package com.nifi.app.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.nifi.app.dto.NifiResponseDto;

@Service
public class NifiGetResourcesAPI implements NifiConnectionAPI<String> {

	@Value("${app.nifi.api.get.resources}")
	private String url;
	
	@Autowired
	private RestTemplate ignoreHttpsRestTemplate;

	@Override
	public NifiResponseDto<String> connect(String... strings) {
		
		String jwt = strings[0];

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setBearerAuth(jwt);

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(headers);

		ResponseEntity<String> token = ignoreHttpsRestTemplate.exchange(url, HttpMethod.GET, entity, String.class);

		return new NifiResponseDto<String>(token.getBody());
	}

}
