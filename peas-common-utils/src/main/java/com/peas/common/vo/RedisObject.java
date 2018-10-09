package com.peas.common.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * Created by dyh on 2015/5/31.
 */
@NoArgsConstructor
@ToString
public class RedisObject {
    @Getter
    private Object object;

    @Setter
    @Getter
    private Class<?> classType;

    @Setter
    @Getter
    private Map<String, RedisObject> fieldMap;

    public RedisObject(Object object) {
        this.object = object;
        this.classType = object.getClass();
    }

    public RedisObject(Object object, Class<?> classType) {
        this.object = object;
        this.classType = classType;
    }

    public void setObject(Object object) {
        this.object = object;
        this.classType = object.getClass();
    }
}
