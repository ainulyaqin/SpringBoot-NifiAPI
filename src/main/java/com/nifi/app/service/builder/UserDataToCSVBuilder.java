package com.nifi.app.service.builder;

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
	private String processGroupId=null;
	private String clientId = UUID.randomUUID().toString();
	
	private String username = "admin";
	private String password = "adminfisclouds";

	@Autowired
	private NifiConnectionFactory nifiConnectionFactory;

	@Override
	public void login() {
		NifiResponseDto<String> token = nifiConnectionFactory.getAPI(EnumNifiAPIType.API_LOGIN).connect(username,
				password);
		this.jwt = token.data();
		log.info("login successfully - jwt created :: 1");
	}

	@Override
	public void getResources() {
		NifiResponseDto<String> body = nifiConnectionFactory.getAPI(EnumNifiAPIType.API_GET_RESOURCE).connect(this.jwt);
		List<Map<String, String>> resources = JsonPath.parse(body.data()).read("$.resources");
		String[] identifierPath = resources.stream().filter(src -> src.get("name").equals("NiFi Flow"))
				.filter(identifier -> identifier.get("identifier").contains("process-groups")).findFirst()
				.map(ident -> ident.get("identifier")).get().split("/");
		this.rootProjectGroupId = identifierPath[identifierPath.length - 1];
		log.info("getResources successfully - rootProjectGroupId created :: 2");
	}

	@Override
	public void createPg(String processGroupName) {
		NifiResponseDto<String> body = nifiConnectionFactory.getAPI(EnumNifiAPIType.API_CREATE_PROCESS_GROUP)
				.connect(this.jwt, this.rootProjectGroupId, processGroupName,clientId);
		this.rootProjectGroupId=JsonPath.parse(body.data()).read("$.id");
		log.info("createPg successfully - processGroupId created :: 3");
	}

	@Override
	public String createProcessor() {
		// TODO Auto-generated method stub
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
