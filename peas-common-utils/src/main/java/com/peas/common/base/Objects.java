package com.peas.common.base;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.peas.hsf.common.Handler;
import lombok.extern.log4j.Log4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author duanyihui
 */
@Log4j
public class Objects {

    private Objects() {
    }

    /**
     * 如果是Null给个默认值
     *
     * @param value        需要判定的值
     * @param defaultValue 如果判定为Null则给的默认值
     * @param <T>
     * @return 值
     */
    public static <T> T nullToDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    /**
     * 判定一个对象是否为空
     *
     * @param o 需要判定的对象
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static boolean isNullOrEmpty(Object o) {
        if (o == null) {
            return true;
        }
        if (o instanceof String || o instanceof StringBuffer || o instanceof StringBuilder) {
            String s = o.toString().trim();
            return Strings.isNullOrEmpty(s);
        }
        if (o instanceof Collection) {
            Collection c = (Collection) o;
            return c.isEmpty();
        }
        if (o instanceof Map) {
            Map m = (Map) o;
            return m.isEmpty();
        }
        if (o.getClass().isArray()) {
            Object[] os = (Object[]) o;
            return os.length == 0;
        }
        if (o instanceof Iterator) {
            Iterator<?> t = (Iterator<?>) o;
            return t.hasNext();
        }
        return false;
    }

    /**
     * 判断一个对象是否在给定的数据内
     *
     * @param target 给定的对象
     * @param os     数据s
     * @param <T>
     * @return
     */
    @SafeVarargs
    public static <T> boolean isIn(T target, T... os) {
        if (os == null || os.length == 0) {
            return false;
        }
        return ImmutableSet.copyOf(os).contains(target);
    }

    /**
     * toString
     *
     * @param o           对象
     * @param nullDefault 如果为Null的默认值
     * @return String
     */
    public static String toString(Object o, String nullDefault) {
        return (o != null) ? o.toString() : nullDefault;
    }

    /**
     * toString
     *
     * @param o 对象
     * @return String
     */
    public static String toString(Object o) {
        return toString(o, "");
    }

    /**
     * 生成UUID
     *
     * @return
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    /**
     * 选择不为NULL的值
     *
     * @param ts  候选值
     * @param <T>
     * @return 第一个不为NULL的值
     */
    @SafeVarargs
    public static <T> T chooseNotNull(T... ts) {
        for (T t : ts) {
            if (!Objects.isNullOrEmpty(t)) {
                return t;
            }
        }
        return null;
    }

    public static <T> T chooseValue(boolean expr, T trueValue, T falseValue) {
        return expr ? trueValue : falseValue;
    }

    public static Date parseDate(String date) {
        Preconditions.checkArgument(!isNullOrEmpty(date), "参数不能为空");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = null;
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            log.error(String.format("格式化日期[%s]错误,正确格式应该为[%s]", date, "yyyy-MM-dd HH:mm:ss"), e);

        }
        return d;
    }

    public final static void freeMemory(Object object) {
        if (object instanceof Collection) {
            Collection c = (Collection) object;
            c.clear();
        } else if (object instanceof Map) {
            Map m = (Map) object;
            m.clear();
        }
        object = null;
    }

    public final static <E, T> T transform(E o, Handler<E, T> t) {
        return t.handle(o);
    }


    public static <T> T[] asArray(T... argument) {
        return argument;
    }


    public final static <K, V> V getValue(K key, Map<K, V> map) {
        return map.get(key);
    }
}
