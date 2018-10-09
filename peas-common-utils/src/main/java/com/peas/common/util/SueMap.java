package com.peas.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;

import java.util.Map;
import java.util.Queue;

/**
 * SueMap
 * Created by duanyihui on 2017/3/31.
 */
public class SueMap
{
    private Map<String, Object> target;

    private SueMap(Map<String, Object> map)
    {
        this.target = map;
    }

    public void put(String key, Object value)
    {
        Queue<String> keys = buildKeys(key);
        Map<String, Object> values = getTarget(target, keys, false);
        Preconditions.checkNotNull(values, "namespace [" + Joiner.on(".").join(keys) + "] not exist");
        values.put(keys.poll(), value);
    }

    public Object get(String key)
    {
        Queue<String> keys = buildKeys(key);
        Map<String, Object> values = getTarget(target, keys, false);
        if (values == null)
        {
            return null;
        }
        return values.get(keys.poll());
    }

    public <T> T get(String key, Class<T> tClass)
    {
        Object o = get(key);
        if (o == null)
        {
            return null;
        }
        Preconditions.checkState(o.getClass().equals(tClass), "Type error,the value is : " + o);
        return (T) o;
    }

    private Queue<String> buildKeys(String key)
    {
        return Queues.newLinkedBlockingDeque(Splitter.on(".").trimResults().split(key));
    }

    public void namespace(String key)
    {
        Queue<String> keys = buildKeys(key);
        Map<String, Object> map = getTarget(target, keys, true);
        String lastKey = keys.poll();
        if (!map.containsKey(lastKey))
        {
            Map<String, Object> lastMap = Maps.newHashMap();
            map.put(lastKey, lastMap);
        }
    }


    public static SueMap decorate(Map<String, Object> map)
    {
        return new SueMap(map);
    }

    public static SueMap decorate(String json)
    {
        Map<String, Object> map = JSON.parseObject(json, new TypeReference<Map<String, Object>>()
        {
        });
        return new SueMap(map);
    }

    private Map<String, Object> getTarget(Map<String, Object> map, Queue<String> keys, boolean autoBuild)
    {
        if (keys.size() == 1)
        {
            return map;
        }
        String key = keys.poll();
        Object value = map.get(key);
        if (value == null && !autoBuild)
        {
            return null;
        }
        else
        {
            if (autoBuild && value == null)
            {
                value = Maps.newHashMap();
                map.put(key, value);
            }
            Preconditions.checkState(value instanceof Map, "value [" + value + "] type is not Map");
            if (keys.size() == 1)
            {
                return (Map<String, Object>) value;
            }
            else
            {
                return getTarget((Map<String, Object>) value, keys, autoBuild);
            }
        }
    }

    @Override
    public String toString()
    {
        return "SueMap" + target;
    }

}
