package com.peas.common.helper.page;

import com.google.common.collect.Maps;
import com.peas.common.base.Eachs;

import java.util.Map;

/**
 * 查询条件参数名称转换为数据库对应列名称
 *
 * @author zhc 2015-2-4
 */
public class AttributeUtils {

    /**
     * 私有构造器
     */
    private AttributeUtils() {

    }

    /**
     * 属性转化为列
     *
     * @param attribute 属性
     * @return
     */
    public static String attribute2column(String attribute) {
        String[] attributes = attribute.split("(?<=[a-z])(?=[A-Z])");
        StringBuffer column = new StringBuffer();

        column.append(attributes[0].toLowerCase());

        for (int i = 1; i < attributes.length; i++) {
            column.append("_");
            column.append(attributes[i].toLowerCase());
        }
        return column.toString();
    }

    /**
     * s-s Map 转换为 s-o Map
     *
     * @param smap
     * @return
     */
    public static Map<String, Object> str2Object(Map<String, String> smap) {
        final Map<String, Object> omap = Maps.newHashMap();
        Eachs.on(smap).each(new Eachs.Item<String, String>() {
            @Override
            public void item(String k, String v) {
                omap.put(k, v);
            }
        });
        return omap;
    }
}
