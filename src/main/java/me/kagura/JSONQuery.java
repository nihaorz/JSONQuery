package me.kagura;

import com.google.gson.*;
import me.kagura.exception.FieldNotExistException;
import me.kagura.exception.TypeNotMismatchException;

import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 核心公共入口
 *
 * @author 鹞之神乐
 * @version 0.2.4
 */
public class JSONQuery {

    /**
     * 返回表达式匹配到的结果
     *
     * @param jsonStr    需要解析的JSON字符串
     * @param expression 表达式
     * @return 返回表达式匹配到的结果，如果表达式为空则将jsonStr转换成JsonResult返回
     * @throws TypeNotMismatchException,FieldNotExistException
     */
    public static JsonResult select(String jsonStr, String expression) throws TypeNotMismatchException, FieldNotExistException {
        return select(new JsonParser().parse(jsonStr), expression);
    }

    /**
     * 返回表达式匹配到的结果
     *
     * @param jsonElement JsonObject/JsonArray/JsonResult/JsonElement
     * @param expression  表达式
     * @return 返回表达式匹配到的结果，如果表达式为空返回将原jsonElement包装后的JsonResult
     * @throws TypeNotMismatchException,FieldNotExistException
     */
    public static <T extends JsonElement> JsonResult select(T jsonElement, String expression) throws TypeNotMismatchException, FieldNotExistException {
        if (expression == null || expression.equals("")) {
            return new JsonResult(jsonElement);
        }
        expression = expression.trim();
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

    /**
     * 返回表达式匹配到的结果并尝试转换为期待的类型
     *
     * @param jsonElement JsonObject/JsonArray/JsonResult/JsonElement
     * @param expression  表达式
     * @param classOfT    期望的结果类型
     * @return 返回表达式匹配到的结果并尝试转换为期待的类型
     * @throws TypeNotMismatchException
     * @throws FieldNotExistException
     */
    public static <T extends JsonElement, S> S select(T jsonElement, String expression, Class<S> classOfT) throws TypeNotMismatchException, FieldNotExistException {
        return new Gson().fromJson(select(jsonElement, expression).getJsonElement(), classOfT);
    }

    /**
     * 返回表达式匹配到的结果并尝试转换为期待的类型
     *
     * @param jsonStr    需要解析的JSON字符串
     * @param expression 表达式
     * @param classOfT   期望的结果类型
     * @return 返回表达式匹配到的结果并尝试转换为期待的类型
     * @throws TypeNotMismatchException
     * @throws FieldNotExistException
     */
    public static <S> S select(String jsonStr, String expression, Class<S> classOfT) throws TypeNotMismatchException, FieldNotExistException {
        return new Gson().fromJson(select(jsonStr, expression).getJsonElement(), classOfT);
    }

    /**
     * 返回表达式匹配到的结果并尝试转换为期待的Type
     *
     * @param jsonElement JsonObject/JsonArray/JsonResult/JsonElement
     * @param expression  表达式
     * @param typeOfT     期望的结果类型Type
     * @return 返回表达式匹配到的结果并尝试转换为期待的Type
     * @throws TypeNotMismatchException
     * @throws FieldNotExistException
     */
    public static <T extends JsonElement, S> S select(T jsonElement, String expression, Type typeOfT) throws TypeNotMismatchException, FieldNotExistException {
        return new Gson().fromJson(select(jsonElement, expression).getJsonElement(), typeOfT);
    }

    /**
     * 返回表达式匹配到的结果并尝试转换为期待的Type
     *
     * @param jsonStr    需要解析的JSON字符串
     * @param expression 表达式
     * @param typeOfT    期望的结果Type
     * @return 返回表达式匹配到的结果并尝试转换为期待的Type
     * @throws TypeNotMismatchException
     * @throws FieldNotExistException
     */
    public static <S> S select(String jsonStr, String expression, Type typeOfT) throws TypeNotMismatchException, FieldNotExistException {
        return new Gson().fromJson(select(jsonStr, expression).getJsonElement(), typeOfT);
    }

}

