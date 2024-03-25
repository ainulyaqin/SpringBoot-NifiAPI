package com.nifi.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public record ResponseTemplate<T> (String message, String code, T data) {
	
	public ResponseTemplate(){
		this("success","0000",null);
	}
	
	public ResponseTemplate(String message){
		this(message,"0000",null);
	}
	
	public ResponseTemplate(T data){
		this("success","0000",data);
	}
}
