package com.nifi.app.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("rawtypes")
public class NifiConnectionFactory {
	
	@Autowired
	private NifiConnectionAPI nifiLoginAPI;
	
	@Autowired
	private NifiConnectionAPI nifiGetResourcesAPI;
	
	@Autowired
	private NifiConnectionAPI nifiCreateProcessGroupAPI;
	
	public NifiConnectionAPI getAPI(EnumNifiAPIType APIType){
		if(APIType.equals(EnumNifiAPIType.API_LOGIN)) {
			return nifiLoginAPI;
		}else if(APIType.equals(EnumNifiAPIType.API_GET_RESOURCE)) {
			return nifiGetResourcesAPI;
		}else if(APIType.equals(EnumNifiAPIType.API_CREATE_PROCESS_GROUP)) {
			return nifiCreateProcessGroupAPI;
		}
		return null;
	}
	
}
