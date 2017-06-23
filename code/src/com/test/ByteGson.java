package com.test;

import java.lang.reflect.Type;

import org.apache.commons.codec.binary.Base64;

import com.gmrz.utils.tlv.util.UtilByte;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ByteGson {

	  public static final Gson customGson = new GsonBuilder().registerTypeHierarchyAdapter(byte[].class,
	            new ByteArrayToBase64TypeAdapter()).create();

	    // Using Android's base64 libraries. This can be replaced with any base64 library.
	    private static class ByteArrayToBase64TypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
	        public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
	            return UtilByte.hex2byte(json.getAsString());
	        }

	        public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
	            return new JsonPrimitive(UtilByte.byte2hex(src));
	        }
	    }
}
