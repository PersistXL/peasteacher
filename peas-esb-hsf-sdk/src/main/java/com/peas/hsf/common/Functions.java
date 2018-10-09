package com.peas.hsf.common;

/**
 * @Author duanyihui
 * @Date 2016/4/8
 * @Description
 */
public class Functions {

    private Functions() {
    }

    public static <T> T returnIf(boolean isOk, T defaultValue, Callback<T> callback) {
        if (isOk) {
            return callback.call();
        }
        return defaultValue;
    }

    public static <T> T callIf(boolean isTrue, Callback<T> trueCall, Callback<T> falseCall) {
        return isTrue ? trueCall.call() : falseCall.call();
    }

    public static void execIf(boolean isTrue, Runnable trueRun, Runnable falseRun) {
        if (isTrue) {
            trueRun.run();
        } else {
            falseRun.run();
        }
    }

    public static void doIf(boolean isTrue, Runnable runnable) {
        if (isTrue) {
            runnable.run();
        }
    }

}
