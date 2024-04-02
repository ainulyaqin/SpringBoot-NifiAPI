package com.nifi.app.service.client;

import java.util.Map;

import com.nifi.app.dto.NifiResponseDto;

public interface NifiConnectionAPI<T> {
	public NifiResponseDto<T> connect(Map<String, Object> parameters);
}
