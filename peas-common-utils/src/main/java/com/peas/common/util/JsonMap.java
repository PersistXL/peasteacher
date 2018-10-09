package com.peas.common.util;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.peas.common.base.Objects;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * JsonMap
 */
public class JsonMap {
    private String json;

    private Map<String, Object> jsonObject;

    JsonMap(String json) {
        this.json = formatJson(json);
        this.jsonObject = JSON.parseObject(this.json, HashMap.class);
    }

    JsonMap() {
        this.jsonObject = Maps.newHashMap();
    }

    private String formatJson(String json) {
        json = Objects.isNullOrEmpty(json) ? "{}" : json;
        return json;
    }

    public JsonMap put(String key, Object value) {
        this.jsonObject.put(key, value);
        return this;
    }

    public boolean containsKey(String key) {
        return jsonObject.containsKey(key);
    }

    public int size() {
        return jsonObject.size();
    }

    public Set<Map.Entry<String, Object>> entrySet() {
        return jsonObject.entrySet();
    }

    public Object getObject(String key) {
        return this.jsonObject.get(key);
    }

    public String getString(String key) {
        return Objects.toString(getObject(key), "");
    }

    public Integer getInt(String key) {
        String i = getString(key, "[0-9]+", Integer.class);
        return i == null ? null : Integer.valueOf(i);
    }

    public Long getLong(String key) {
        String i = getString(key, "[0-9]+", Long.class);
        return i == null ? null : Long.valueOf(i);
    }

    public Double getDouble(String key) {
        String i = getString(key, "[0-9]+\\.[0-9]+", Double.class);
        return i == null ? null : Double.valueOf(i);
    }

    public Boolean getBoolean(String key) {
        String v = getString(key, "true|false", Boolean.class);
        return Boolean.valueOf(v);
    }

    public <T> T get(String key, Class<T> t) {
        String v = getString(key);
        Preconditions.checkArgument(
                !(t.getName().matches("java\\.lang\\.(Integer|Long|String|Character|Byte|Short|Float|Double)") && t
                        .isArray()), "can not cast be basic type");
        v = Objects.isNullOrEmpty(v) ? "{}" : v;
        return JSON.parseObject(v, t);
    }

    public JsonMap getMap(String key) {
        String m = getString(key);
        return new JsonMap(m);
    }

    public JsonList getList(String key) {
        String m = getString(key);
        return new JsonList(m);
    }

    public Map<String, Object> toMap() {
        return Maps.newHashMap(jsonObject);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(jsonObject);
    }

    private String getString(String key, String match, Class<?> type) {
        String s = getString(key);
        if (Objects.isNullOrEmpty(s)) {
            return null;
        }
        Preconditions.checkState(s.toLowerCase().matches(match), "value [" + s + "] can not cast be " + type.getName());
        return s;
    }
}