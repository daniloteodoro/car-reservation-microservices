package com.reservation.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import java.io.IOException;
import java.util.Objects;

public class JsonUtils {

	private static ObjectMapper mapper = buildMapper();

	private static ObjectMapper buildMapper() {
		return new ObjectMapper()
				.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
	}
	
	public static String toJson(Object obj) {
		Objects.requireNonNull(obj, "Cannot serialize null object to JSON");
		
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static <T> T toObject(String json, Class<T> targetClass) {
		try {
			return mapper.readValue(json, targetClass);
		} catch (IOException e) {
			throw new RuntimeException(String.format("Failure deserializing json: %s\n%s\n", e.getMessage(), json), e);
		}
	}

}
