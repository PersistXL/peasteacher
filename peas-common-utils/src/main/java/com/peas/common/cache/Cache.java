package com.peas.common.cache;

import com.google.common.cache.CacheBuilder;
import lombok.extern.log4j.Log4j;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Log4j
public class Cache<T> {

    private com.google.common.cache.Cache<String, T> cache;

    private CacheLoader<T> loader;

    Cache(int size, int time, TimeUnit timeUnit) {
        cache = CacheBuilder.newBuilder()
                .maximumSize(size)
                .weakValues()
                .weakKeys()
                .expireAfterWrite(time, timeUnit)
                .build();
    }


    Cache(int size, int time, TimeUnit timeUnit, CacheLoader<T> loader) {
        this(size, time, timeUnit);
        this.loader = loader;
    }


    public T get(String key, Callable<T> callable) {
        try {
            return cache.get(key, callable);
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    public T get(String key) {
        try {
            return cache.get(key, () -> loader.load(key));
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    public void invalidate(Object key) {
        cache.invalidate(key);
    }

    public void invalidateAll() {
        cache.invalidateAll();
    }

    public void invalidateAll(Iterable<?> keys) {
        cache.invalidateAll(keys);
    }

    public void put(String key, T value) {
        cache.put(key, value);
    }

    public long size() {
        return cache.size();
    }

    public interface CacheLoader<T> {
        T load(String s);
    }
}