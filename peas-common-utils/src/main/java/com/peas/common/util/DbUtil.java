package com.peas.common.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import org.apache.commons.lang3.CharUtils;

import com.google.common.collect.Maps;

/**
 * 数据库Util
 * Created by Sue on 2017/3/26.
 */
public class DbUtil
{
    /**
     * 转化为数据库字段
     *
     * @param bean
     * @return
     */
    public static Map<String, Object> toColumn(Object bean)
    {
        Map<String, Object> map = Maps.newHashMap();
        try
        {
            BeanInfo info = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : descriptors)
            {
                Method getter = descriptor.getReadMethod();
                Method setter = descriptor.getWriteMethod();
                if (getter == null || setter == null)
                {
                    continue;
                }
                map.put(propertyToColumn(descriptor.getName()), getter.invoke(bean));
            }
        }
        catch (IntrospectionException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 转换为java bean
     *
     * @param map
     * @param beanClass
     * @param <T>
     * @return
     */
    public static <T> T toProperty(Map<String, Object> map, Class<T> beanClass)
    {
        T bean = null;
        try
        {
            bean = beanClass.newInstance();
            BeanInfo info = Introspector.getBeanInfo(beanClass);
            PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : descriptors)
            {
                Method getter = descriptor.getReadMethod();
                Method setter = descriptor.getWriteMethod();
                if (getter == null || setter == null)
                {
                    continue;
                }
                setter.invoke(bean, map.get(propertyToColumn(descriptor.getName())));
            }
        }
        catch (IntrospectionException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return bean;
    }


    /**
     * 对象属性转换为字段  例如：userName to user_name
     *
     * @param property 字段名
     * @return
     */
    public static String propertyToColumn(String property)
    {
        if (null == property)
        {
            return "";
        }
        char[] chars = property.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (char c : chars)
        {
            if (CharUtils.isAsciiAlphaUpper(c))
            {
                sb.append("_" + CharUtils.toString(c).toLowerCase());
            }
            else
            {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 字段转换成对象属性 例如：user_name to userName
     *
     * @param field
     * @return
     */
    public static String columnToProperty(String field)
    {
        if (null == field)
        {
            return "";
        }
        char[] chars = field.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < chars.length; i++)
        {
            char c = chars[i];
            if (c == '_')
            {
                int j = i + 1;
                if (j < chars.length)
                {
                    sb.append(CharUtils.toString(chars[j]).toUpperCase());
                    i++;
                }
            }
            else
            {
                sb.append(c);
            }
        }
        return sb.toString();
    }


}
