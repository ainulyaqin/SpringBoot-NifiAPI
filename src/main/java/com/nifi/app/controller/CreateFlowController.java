package com.nifi.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nifi.app.dto.ResponseTemplate;
import com.nifi.app.service.CreateFlowService;

@RestController
public class CreateFlowController {
	
	@Autowired
	private CreateFlowService createFlowService;
	
	@SuppressWarnings("rawtypes")
	@GetMapping("/api/v1/create-flow")
	public ResponseTemplate createFlow(String processGroupName) {
		return createFlowService.createFlow(processGroupName);
	}
	

}
