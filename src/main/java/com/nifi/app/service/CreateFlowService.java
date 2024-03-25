package com.nifi.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nifi.app.dto.ResponseTemplate;
import com.nifi.app.service.builder.FlowBuilder;

@SuppressWarnings({ "rawtypes" })
@Service
public class CreateFlowService {

	@Autowired
	private FlowBuilder userDataToCSVBuilder;
	
	public ResponseTemplate createFlow(String processGroupName) {
		
		userDataToCSVBuilder.login();
		
		userDataToCSVBuilder.getResources();
		
		userDataToCSVBuilder.createPg(processGroupName);
		
		return new ResponseTemplate("Flow successfully created");
	}

}
