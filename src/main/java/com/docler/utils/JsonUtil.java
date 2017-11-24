package com.docler.utils;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

public class JsonUtil {
	public static <T> String convertToString(T object) {
		ObjectMapper mapper = new ObjectMapper();

		//Object to JSON in String
		try {
			String jsonString = mapper.writeValueAsString(object);
			return jsonString;
		} catch (IOException e) {			
			e.printStackTrace();
			return "";
		}
	}
}
