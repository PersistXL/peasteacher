package com.peas.common.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.peas.common.base.handler.KeyValueHandler;
import com.peas.common.base.handler.ProcessHandler;
import com.peas.common.vo.KeyValuePair;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by dyh on 2015/5/31.
 */
public class MapValues
{

    public static void clearKeys(Map<String, ?> map, String matcher)
    {
        List<String> keys = Lists.newArrayList();
        map.forEach((key, value) ->
        {
            if (key.matches(matcher))
            {
                keys.add(key);
            }
        });
        keys.forEach(k -> map.remove(k));
    }

    public static <T> Values values()
    {
        return new Values<T>().values();
    }

    public static <T> Values set(String key, T value)
    {
        return values().set(key, value);
    }

    public static String messageJson(int code, String message, Object resultObject)
    {
        return create(null, code, message, resultObject).toJson();
    }

    public static Values message(int code, String message, Object resultObject)
    {
        return create(null, code, message, resultObject);
    }

    public static String messageJson(int code, Object data)
    {
        return create(null, code, null, data).toJson();
    }

    public static Values message(int code, Object data)
    {
        return create(null, code, null, data);
    }

    public static String error(int code, String message)
    {
        return Values.values().set("code", code).set("message", message).set("type", "CHECK_RUNTIME_EXCEPTION").toJson();
    }

    private static Values create(String result, int code, String message, Object resultObject)
    {
        Values mv = values().set("status", result).set("code", code).set("message", message);
        if (!Objects.isNullOrEmpty(resultObject))
        {
            mv.set("data", resultObject);
        }
        return mv;
    }

    public static class Values<T>
    {
        private final Map<String, T> values;

        private final SerializeConfig serializeConfig = new SerializeConfig();

        private List<KeyValueHandler> handlers = Lists.newArrayList();

        private Values()
        {
            this(Maps.newHashMap());
        }

        private Values(Map<String, T> maps)
        {
            values = maps;
        }

        public static Values values()
        {
            return new Values();
        }

        public static Values values(Map<String, ?> map)
        {
            Preconditions.checkNotNull(map, "map can not be null");
            return new Values(Maps.newHashMap(map));
        }

        public Values withKeyValueHandler(KeyValueHandler<String, Object> handler)
        {
            handlers.add(handler);
            return this;
        }

        public Values set(String key, T value)
        {
            set(handle(key, value));
            return this;
        }

        private void set(KeyValuePair<String, T> kvp)
        {
            values.put(kvp.getKey(), kvp.getValue());
        }

//        public Values set(String key, Number value) {
//            set(handle(key, value));
//            return this;
//        }
//
//        public Values set(String key, Object value) {
//            set(handle(key, value));
//            return this;
//        }

        public void clear()
        {
            this.values.clear();
        }

        public Map<String, Object> toMap()
        {
            return Maps.newHashMap(this.values);
        }

        public Values addHandler(Type type, Handler handler)
        {
            serializeConfig.put(type, new ObjectSerializer()
            {
                @Override
                public void write(JSONSerializer jsonSerializer, Object value, Object name, Type type) throws
                                                                                                       IOException
                {
                    jsonSerializer.getWriter().write(JSON.toJSONString(handler.handle(value, Objects.toString(name))));
                }
            });
            return this;
        }

        public String toJson()
        {
            return JSON.toJSONString(this.values, serializeConfig);
        }


        public String toJsonStringWithDateFormat(String dateFormatter)
        {
            return JSON.toJSONStringWithDateFormat(this.values, dateFormatter);
        }

        public String toString()
        {
            return this.values.toString();
        }

        private KeyValuePair<String, T> handle(final String key, final T value)
        {
            KeyValuePair<String, T> kvp = new KeyValuePair<>(key, value);
            for (KeyValueHandler kv : handlers)
            {
                kvp = kv.handler(kvp.getKey(), kvp.getValue());
            }
            return kvp;
        }

        public <T> T build(ProcessHandler<T> handler)
        {
            return handler.handle(toMap());
        }
    }

    public static interface Handler
    {
        String handle(Object value, String fieldName);
    }

}
