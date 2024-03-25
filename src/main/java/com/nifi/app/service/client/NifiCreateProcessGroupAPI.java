package com.nifi.app.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nifi.app.dto.NifiResponseDto;

@Service
public class NifiCreateProcessGroupAPI implements NifiConnectionAPI<String> {

	@Value("${app.nifi.api.create.progressGroup}")
	private String url;
	
	@Autowired
	private RestTemplate ignoreHttpsRestTemplate;

	@Override
	public NifiResponseDto<String> connect(String... strings) {

		String jwt = strings[0];
		String rootPgId = strings[1];
		String processGroupName = strings[2];
		String clientId=strings[3];
		this.url = this.url.replace("ROOT_PG_ID", rootPgId);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(jwt);
		
		Component component = new Component(processGroupName, new Position(300, -300));
		RequestBody requestBody = new RequestBody(new Revision(clientId,0), component);
		HttpEntity<RequestBody> entity = new HttpEntity<>(requestBody,headers);

		ResponseEntity<String> token = ignoreHttpsRestTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		return new NifiResponseDto<String>(token.getBody());
	}
	
	record RequestBody (Revision revision, Component component) {}
	record Revision (String clientId, Integer version) {}
	record Component (String name, Position position) {}
	record Position (int x, int y) {}
}
