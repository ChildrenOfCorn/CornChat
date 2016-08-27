package ru.cft.chuldrenofcorn.cornchat.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * User: asmoljak
 * Date: 26.08.2016
 * Time: 23:46
 */
//TODO: Remove me
public final class Config {
//	public static final String USER_EAN = "operator02";
	public static final String OPERATOR_ID = "router";
	public static final String SERVER_HOST = "andrey-laptop";
	public static final String USER_EAN ="2960291738335";

	private static final JsonSerializer<Date> ser = new JsonSerializer<Date>() {
		@Override
		public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext
				context) {
			return src == null ? null : new JsonPrimitive(src.getTime());
		}
	};

	private static final JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {
		@Override
		public Date deserialize(JsonElement json, Type typeOfT,
								JsonDeserializationContext context) throws JsonParseException {
			return json == null ? null : new Date(json.getAsLong());
		}
	};

	public static boolean isBot(final String userName) {
		return  "Bender".equals(userName);
	}


	public static final Gson gson = new GsonBuilder()
			.registerTypeAdapter(Date.class, ser)
			.registerTypeAdapter(Date.class, deser).create();
}
