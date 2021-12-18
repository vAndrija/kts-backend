package com.kti.restaurant.utils;

import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {

	
	
	public static String ToJson(Object entity) {
		GsonBuilder builder = new GsonBuilder();

		builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());

		builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());

		builder.serializeNulls();
		Gson gson = builder.create();
		
		return gson.toJson(entity);

	}
}