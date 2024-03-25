package com.nifi.app.service.builder;

public interface FlowBuilder {
	
	public void login();
	
	public void getResources();
	
	public void createPg(String processGroupName);
	
	public String createProcessor();
	
	public String createConnection();
	
	public String runFlow();
}
