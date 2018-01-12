package me.kagura.exception;

import com.google.gson.JsonElement;

public class TypeNotMismatchException extends Exception {

    public TypeNotMismatchException(String expected, JsonElement jsonElement) {
        super("Inconsistent with the expected type. expected:" + expected + "\treal:"
                + (jsonElement.isJsonArray() ? "JsonArray" : "")
                + (jsonElement.isJsonObject() ? "JsonObject" : "")
                + (jsonElement.isJsonNull() ? "JsonNull" : "")
                + (jsonElement.isJsonPrimitive() ? "JsonPrimitive" : "")
        );
    }

}
