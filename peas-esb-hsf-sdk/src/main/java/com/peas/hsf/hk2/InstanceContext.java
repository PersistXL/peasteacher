package com.peas.hsf.hk2;

import com.google.inject.Injector;

/**
 * Created by duanyihui on 2017/2/28.
 */
public class InstanceContext {

    private static Injector context;

    public static <T> T getInstance(Class<T> clz) {
        return context.getInstance(clz);
    }

    static void addContext(Injector context) {
        InstanceContext.context = context;
    }

}
