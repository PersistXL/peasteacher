package com.peas.hsf.common;

/**
 * Created by duanyihui on 2016/7/15.
 */
public interface Handler<E, T> {
    T handle(E e);
}
