package me.kagura;

import com.google.gson.*;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author 鹞之神乐
 * @version 0.2.3
 */
public class JsonResult extends JsonElement {

    private JsonElement jsonElement;

    public JsonResult(JsonElement jsonElement) {
        this.jsonElement = jsonElement;
    }

    public JsonElement getJsonElement() {
        return jsonElement;
    }

    public JsonElement deepCopy() {
        return jsonElement;
    }

    public boolean isJsonArray() {
        return jsonElement.isJsonArray();
    }

    public boolean isJsonObject() {
        return jsonElement.isJsonObject();
    }

    public boolean isJsonPrimitive() {
        return jsonElement.isJsonPrimitive();
    }

    public boolean isJsonNull() {
        return jsonElement.isJsonNull();
    }

    public JsonObject getAsJsonObject() {
        return jsonElement.getAsJsonObject();
    }

    public JsonArray getAsJsonArray() {
        return jsonElement.getAsJsonArray();
    }

    public JsonPrimitive getAsJsonPrimitive() {
        return jsonElement.getAsJsonPrimitive();
    }

    public JsonNull getAsJsonNull() {
        return jsonElement.getAsJsonNull();
    }

    public String getAsString() {
        return jsonElement.getAsString();
    }

    public double getAsDouble() {
        return jsonElement.getAsDouble();
    }

    public BigDecimal getAsBigDecimal() {
        return jsonElement.getAsBigDecimal();
    }

    public BigInteger getAsBigInteger() {
        return jsonElement.getAsBigInteger();
    }

    public float getAsFloat() {
        return jsonElement.getAsFloat();
    }

    public long getAsLong() {
        return jsonElement.getAsLong();
    }

    public short getAsShort() {
        return jsonElement.getAsShort();
    }

    public int getAsInt() {
        return jsonElement.getAsInt();
    }

    public byte getAsByte() {
        return jsonElement.getAsByte();
    }

    public char getAsCharacter() {
        return jsonElement.getAsCharacter();
    }

    public boolean getAsBoolean() {
        return jsonElement.getAsBoolean();
    }

    public String toString() {
        if (jsonElement.isJsonPrimitive()) {
            if (jsonElement.getAsJsonPrimitive().isString()) {
                return jsonElement.getAsString();
            }
        }
        return jsonElement.toString();
    }

}