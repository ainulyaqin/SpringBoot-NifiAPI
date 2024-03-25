package com.nifi.app.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.nifi.app.dto.NifiResponseDto;

@Service
public class NifiLoginAPI implements NifiConnectionAPI<String> {

	@Value("${app.nifi.api.login}")
	private String url;
	
	@Value("${app.nifi.username}")
	private String username;
	
	@Value("${app.nifi.password}")
	private String password;
	
	@Autowired
	private RestTemplate ignoreHttpsRestTemplate;

	@Override
	public NifiResponseDto<String> connect(String... strings) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("username", username);
		map.add("password", password);

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map,headers);

		ResponseEntity<String> token = ignoreHttpsRestTemplate.exchange(url, HttpMethod.POST, entity, String.class);

		return new NifiResponseDto<String>(token.getBody());
	}

}
