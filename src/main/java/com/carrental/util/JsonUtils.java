package com.carrental.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import io.netty.util.internal.ObjectUtil;

public class JsonUtils {
	
	public static String toJson(Object obj) {
		ObjectUtil.checkNotNull(obj, "Cannot serialize null object to JSON");
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "";
		}
	}

}
