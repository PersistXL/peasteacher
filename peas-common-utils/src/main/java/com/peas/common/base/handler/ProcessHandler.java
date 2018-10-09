package com.peas.common.base.handler;

import java.util.Map;

/**
 * Created by dyh on 2015/5/31.
 */
public interface ProcessHandler<T> {
    public T handle(Map<String, Object> kvs);
}
