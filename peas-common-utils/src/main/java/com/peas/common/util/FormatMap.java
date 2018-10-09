package com.peas.common.util;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Created by xiekunliang on 2017/5/19.
 */
public class FormatMap
{

    public static Map<String, Object> format(Object src, String... key)
    {
        Map srcMap;
        if (src instanceof Map)
        {
            srcMap = (Map) src;
        }
        else
        {
            srcMap = BeanUtil.toMap(src);
        }
        Map<String, Object> destMap = Maps.newHashMap();
        for (String k : key)
        {
            if (srcMap.containsKey(k))
            {
                destMap.put(k, srcMap.get(k));
            }
        }
        return destMap;
    }
}
