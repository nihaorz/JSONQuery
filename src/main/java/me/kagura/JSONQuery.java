package me.kagura;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.kagura.exception.FieldNotExistException;
import me.kagura.exception.TypeNotMismatchException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The core public access point to the JSONQuery functionality.
 *
 * @author 鹞之神乐
 * @version 0.2.3
 */
public class JSONQuery {

    /**
     * Find elements matching selector.
     *
     * @param jsonStr    JsonString root element to descend intos
     * @param expression selector
     * @return matching elements, empty if none
     * @throws TypeNotMismatchException,FieldNotExistException
     */
    public static JsonResult select(String jsonStr, String expression) throws TypeNotMismatchException, FieldNotExistException {
        return select(new JsonParser().parse(jsonStr), expression);
    }

    /**
     * Find elements matching selector.
     *
     * @param jsonElement root element to descend intos
     * @param expression  selector
     * @return matching elements, empty if none
     * @throws TypeNotMismatchException,FieldNotExistException
     */
    public static <T extends JsonElement> JsonResult select(T jsonElement, String expression) throws TypeNotMismatchException, FieldNotExistException {
        expression = expression.trim();
        if (expression.equals("") || expression == null) {
            return new JsonResult(jsonElement);
        }
        JsonElement tempJsonElement = jsonElement.deepCopy();
        String[] split = expression.split(">");
        for (int i = 0; i < split.length; i++) {
            String tempKey = split[i];
            if (tempKey.trim().matches("\\[-?\\d+\\]")) {
                if (!tempJsonElement.isJsonArray()) {
                    throw new TypeNotMismatchException("JsonArray", tempJsonElement);
                }
                JsonArray jsonArray = tempJsonElement.getAsJsonArray();
                Matcher matcher = Pattern.compile("\\[(-?\\d+)\\]").matcher(tempKey.trim());
                matcher.find();
                int index = Integer.parseInt(matcher.group(1));
                tempJsonElement = jsonArray.get(index > 0 ? index : jsonArray.size() + index);
            } else if (tempKey.trim().matches("\\[[\\s\\S]+\\]")) {
                if (!tempJsonElement.isJsonObject()) {
                    throw new TypeNotMismatchException("JsonObject", tempJsonElement);
                }
                JsonArray tempArray = new JsonArray();
                Matcher matcher = Pattern.compile("\\[([\\s\\S]+)\\]").matcher(tempKey.trim());
                matcher.find();
                String reg = matcher.group(1);
                JsonObject jsonObject = tempJsonElement.getAsJsonObject();
                for (String key : jsonObject.keySet()) {
                    if (key.matches(reg)) {
                        tempArray.add(jsonObject.get(key));
                    }
                }
                tempJsonElement = tempArray;
            } else {
                if (!tempJsonElement.isJsonObject()) {
                    throw new TypeNotMismatchException("JsonObject", tempJsonElement);
                }
                if (tempJsonElement.isJsonObject()) {
                    if (!tempJsonElement.getAsJsonObject().has(tempKey)) {
                        tempKey = tempKey.trim();
                        if (!tempJsonElement.getAsJsonObject().has(tempKey)) {
                            throw new FieldNotExistException(tempKey);
                        }
                    }
                    tempJsonElement = tempJsonElement.getAsJsonObject().get(tempKey);
                }
                if (tempJsonElement.isJsonPrimitive()) {
                    String string = tempJsonElement.toString();
                    if ((string.matches("^\"\\s*\\{[\\s\\S]*") && string.matches("[\\s\\S]*\\}\\s*\"")) || (string.matches("^\"\\s*\\[[\\s\\S]*") && string.matches("[\\s\\S]*\\]\\s*\""))) {
                        tempJsonElement = new JsonParser().parse(tempJsonElement.getAsString());
                    }

                }

            }

        }
        return new JsonResult(tempJsonElement);
    }

}

