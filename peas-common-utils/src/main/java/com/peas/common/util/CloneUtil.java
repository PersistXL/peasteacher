package com.peas.common.util;

import com.rits.cloning.Cloner;

/**
 * Created by duan on 2015/12/18.
 */
public class CloneUtil {

    private static final Cloner cloner = new Cloner();

    private CloneUtil() {
    }

    public static <T> T deepClone(T o) {
        return cloner.deepClone(o);
    }

}
