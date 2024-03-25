package com.nifi.app.service.client;

import com.nifi.app.dto.NifiResponseDto;

public interface NifiConnectionAPI<T> {
	public NifiResponseDto<T> connect(String...strings);
}
