package com.yunpian.sdk.util;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


/**
 * json格式化工具
 */
public class JsonUtil {
    private static final Gson gson =
        new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public static <T> T fromJson(String json, Class clazz) {
        return (T) gson.fromJson(json, clazz);
    }
    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static Map<String, String> fromJsonToMap(String json) {
        // CHECKSTYLE:OFF
        Type t = new TypeToken<Map<String, String>>() {
        }.getType();
        // CHECKSTYLE:ON
        return gson.fromJson(json, t);
    }
    public static <T> T fromJson(String json, Type t) {
        return gson.fromJson(json, t);
    }
}
