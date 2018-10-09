package com.peas.hsf.tool;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

/**
 * @Author duanyihui
 * @Date 2016/4/5
 * @Description
 */
public class GsonUtil {

    private static final Gson gson;

    static {
        gson = new Gson();
    }

    public static <T> T parse(String json, TypeToken<T> typeToken) {
        return gson.fromJson(json, typeToken.getType());
    }

    public static Map<String, Object> parseToMap(String json) {
        return parse(json, new TypeToken<Map<String, Object>>() {
        });
    }


    public static boolean isJsonObject(String json) {
        try {
            gson.fromJson(json, Object.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String toJson(Object obj)
    {
        return gson.toJson(obj);
    }
}
