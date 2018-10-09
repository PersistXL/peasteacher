package com.peas.common.util;

import com.google.common.collect.Maps;
import com.peas.common.vo.RedisObject;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * Created by dyh on 2015/5/31.
 */
class RedisObjectUtil {

    public static RedisObject toRedisObject(Object object)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        RedisObject ro = new RedisObject(object);
        BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
        PropertyDescriptor[] ps = beanInfo.getPropertyDescriptors();
        Map<String, RedisObject> map = Maps.newHashMap();
        for (PropertyDescriptor p : ps) {
            Method readMethod = p.getReadMethod();
            if (Modifier.isNative(readMethod.getModifiers())) {
                continue;
            }
            String name = p.getName();
            Object value = readMethod.invoke(object);
            Class<?> clazz = value == null ? p.getPropertyType() : value.getClass();
            if (isBasicType(clazz)) {
                map.put(name, new RedisObject(value, clazz));
            } else if (isCollection(clazz)) {
                map.put(name, new RedisObject(value, clazz));
            } else if (isArray(clazz)) {
                map.put(name, new RedisObject(value, clazz));
            } else if (isMap(clazz)) {

            } else {
                map.put(name, toRedisObject(value));
            }
        }
        return ro;
    }

    private static boolean isBasicType(Class<?> clazz) {
        return clazz.getName().matches("^java\\.lang\\.(Integer|Long|Byte|Short|Character|String|Float|Double)$");
    }

    private static boolean isCollection(Class<?> clazz) {
        return clazz.getName().matches("^java\\.util\\.(concurrent\\.)?\\w*(List|Queue|Stack)$");
    }

    private static boolean isMap(Class<?> clazz) {
        return clazz.getName().matches("^java\\.util\\.(concurrent\\.)?\\w*Map$");
    }

    private static boolean isArray(Class<?> clazz) {
        return clazz.isArray();
    }
}
