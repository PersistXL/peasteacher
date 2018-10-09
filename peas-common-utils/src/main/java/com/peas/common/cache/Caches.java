package com.peas.common.cache;

import com.peas.common.util.MD5Util;
import lombok.extern.log4j.Log4j;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存
 * Created by duan on 2015/12/18.
 */
@Log4j
public class Caches {


    public static <T> Cache<T> newCache() {
        return new Cache<>(100, 30, TimeUnit.MINUTES);
    }

    public static <T> Cache<T> newCache(int size, int time, TimeUnit timeUnit) {
        return new Cache<>(size, time, timeUnit);
    }

    public static <T> Cache<T> newCache(int size, int time, TimeUnit timeUnit, Cache.CacheLoader<T> loader) {
        return new Cache<>(size, time, timeUnit, loader);
    }

    public static String createCacheKey(Object... objs) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : objs) {
            sb.append(obj);
        }
        return "$Cache-" + MD5Util.sign(sb.toString().getBytes(Charset.forName("utf-8")));
    }
}

