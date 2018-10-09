package com.peas.common.util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.peas.common.base.Objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dyh on 2015/6/2.
 */
public class JsonList {
    private String json;

    private List<Object> list;

    JsonList() {
        this.list = Lists.newArrayList(list);
    }

    JsonList(String json) {
        this.json = json;
        this.list = JSON.parseObject(json, ArrayList.class);
    }

    public String getString(int index) {
        return Objects.toString(this.list.get(index));
    }

    public int getInt(int index) {
        String v = getString(index);
        return Integer.valueOf(v);
    }

    public double getDouble(int index) {
        String v = getString(index);
        return Double.valueOf(v);
    }

    public boolean getBoolean(int index) {
        String v = getString(index);
        return Boolean.valueOf(v);
    }

    public JsonMap getMap(int index) {
        String v = getString(index);
        return new JsonMap(v);
    }

    public JsonList getList(int index) {
        String v = getString(index);
        return new JsonList(v);
    }

    public List<Object> toList() {
        return Lists.newArrayList(this.list);
    }

    public <T> List<T> toList(Class<T> t) {
        List<T> l = Lists.newArrayList();
        for (Object o : this.list) {
            l.add(JSON.parseObject(o.toString(), t));
        }
        return l;
    }

    public String toString() {
        return this.json;
    }
}
