package com.nifi.app.service.builder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jayway.jsonpath.JsonPath;
import com.nifi.app.dto.NifiResponseDto;
import com.nifi.app.service.client.EnumNifiAPIType;
import com.nifi.app.service.client.NifiConnectionFactory;

@SuppressWarnings("unchecked")
@Service
public class UserDataToCSVBuilder implements FlowBuilder {

	private Log log = LogFactory.getLog(UserDataToCSVBuilder.class);

	private String jwt = null;
	private String rootProjectGroupId = null;
	private String processGroupId = null;
	private String clientId = UUID.randomUUID().toString();

	@Autowired
	private NifiConnectionFactory nifiConnectionFactory;

	@Override
	public void login() {
		Map<String, String> params = new HashMap<String, String>();
		NifiResponseDto<String> token = nifiConnectionFactory.getAPI(EnumNifiAPIType.API_LOGIN).connect(params);
		this.jwt = token.data();
		log.info("login successfully - jwt created");
	}

	@Override
	public void getResources() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("jwt", this.jwt);

		NifiResponseDto<String> body = nifiConnectionFactory.getAPI(EnumNifiAPIType.API_GET_RESOURCE).connect(params);
		List<Map<String, String>> resources = JsonPath.parse(body.data()).read("$.resources");
		String[] identifierPath = resources.stream().filter(src -> src.get("name").equals("NiFi Flow"))
				.filter(identifier -> identifier.get("identifier").contains("process-groups")).findFirst()
				.map(ident -> ident.get("identifier")).get().split("/");
		this.rootProjectGroupId = identifierPath[identifierPath.length - 1];
		log.info("getResources successfully - rootProjectGroupId : " + this.rootProjectGroupId);
	}

	@Override
	public void createPg(String processGroupName) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("jwt", this.jwt);
		params.put("rootProjectGroupId", this.rootProjectGroupId);
		params.put("processGroupName", processGroupName);
		params.put("clientId", clientId);
		NifiResponseDto<String> body = nifiConnectionFactory.getAPI(EnumNifiAPIType.API_CREATE_PROCESS_GROUP).connect(params);
		this.processGroupId = JsonPath.parse(body.data()).read("$.id");
		log.info("createPg successfully - processGroupId created : " + this.processGroupId);
	}

	@Override
	public String createProcessor() {
		
		/*
		 * create processor INVOKE HTTP
		 */
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("jwt", this.jwt);
		params.put("clientId", this.clientId);
		params.put("processGroupId", this.processGroupId);

		Map<String, String> configProperties = new HashMap<String, String>();
		configProperties.put("Remote URL", "https://631960878e51a64d2be34e31.mockapi.io/api/v1/users");
		configProperties.put("HTTP Method", "GET");
		
		params.put("configProperties", configProperties);
		
		NifiResponseDto<String> body = nifiConnectionFactory.getAPI(EnumNifiAPIType.API_CREATE_PROCESSOR_INVOKE_HTTP).connect(params);
		log.info("create processor successfully - InvokeHTTP created ");

		return null;
	}

	@Override
	public String createConnection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String runFlow() {
		return this.rootProjectGroupId;
	}

}
