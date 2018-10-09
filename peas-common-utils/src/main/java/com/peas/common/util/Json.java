package com.peas.common.util;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Splitter;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;

/**
 * Created by dyh on 2015/5/27.
 */
public class Json {
    private Json() {
    }

    /**
     * 创建JsonMap
     *
     * @param json json字符串
     * @return
     */
    public static JsonMap newJsonMap(String json) {
        return new JsonMap(json);
    }

    /**
     * 创建JsonList
     *
     * @param json json字符串
     * @return
     */
    public static JsonList newJsonList(String json) {
        return new JsonList(json);
    }

    /**
     * 创建JsonMap
     *
     * @return
     */
    public static JsonMap newJsonMap() {
        return new JsonMap();
    }

    /**
     * 创建JsonList
     *
     * @return
     */
    public static JsonList newJsonList() {
        return new JsonList();
    }

    /**
     * 序列化对象为Json串
     *
     * @param value
     * @return
     */
    public static String serialize(Object value) {
        return JSON.toJSONString(value);
    }

    /**
     * 序列化为字节数组
     */
    public static byte[] serializeToByteArray(Object value) {
        return JSON.toJSONBytes(value);
    }

    /**
     * 反序列化json为对象
     *
     * @param json json字符串
     * @param c    转为的类
     * @param <T>
     * @return
     */
    public static <T> T deserialize(String json, Class<T> c) {
        return JSON.parseObject(json, c);
    }

    /**
     * 反序列化为数组
     *
     * @param jsonByteArray json字节数组
     * @param c
     * @param <T>
     * @return
     */
    public static <T> T deserialize(byte[] jsonByteArray, Class<T> c) {
        return JSON.parseObject(jsonByteArray, c);
    }

    public static String formatJson(String str) {
        return str.replaceAll("\\s", "");
    }

    /**
     * 转化复杂Json为对象
     *
     * @param maps ex: <k="a.b.c">  json:{a:b{c:{}}}
     * @param t    转换的类型
     * @param <T>
     * @return
     */
    public static <T> T to(Map<String, Object> maps, Class<T> t) {
        T result = null;
        try {
            result = t.newInstance();
            for (Map.Entry<String, Object> e : maps.entrySet()) {
                Queue<String> queue = new ArrayDeque<>();
                queue.addAll(Splitter.on(".").splitToList(e.getKey()));
                setValue(t, result, queue, e.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static void setValue(Class<?> clazz, Object object, Queue<String> fields, Object fieldValue)
            throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        if (fields.isEmpty()) {
            return;
        }
        String field = fields.poll();
        Field f = clazz.getDeclaredField(field);
        f.setAccessible(true);
        Object v = f.get(object);
        Class<?> fieldType = f.getType();
        if (fields.size() == 0) {
            f.set(object, fieldValue);
        } else {
            if (v == null) {
                v = fieldType.newInstance();
                f.set(object, v);
            }
            setValue(fieldType, v, fields, fieldValue);
        }
    }


    private static boolean isBasicType(Class<?> c) {
        return c.getName().matches("java\\.lang\\.(Integer|Long|String|Double|Boolean|Short|Character|Byte)");
    }
}
