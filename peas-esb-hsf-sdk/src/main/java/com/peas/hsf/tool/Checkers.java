package com.peas.hsf.tool;

import com.peas.hsf.exception.CheckerException;
import com.peas.hsf.http.ErrorCode;
import com.peas.hsf.model.Message;

/**
 * 响应工具
 *
 * @author dyh on 2015/2/5.
 */
public class Checkers {

    /**
     * 私有构造器
     */
    private Checkers() {
    }


    public static void checkArgument(boolean expr, int errorCode, String message, boolean isReturnSame) {
        if (!expr) {
            throw new CheckerException(new Message(message, errorCode, isReturnSame));
        }
    }

    public static void checkArgument(boolean expr, String message, boolean isReturnSame) {
        if (!expr) {
            throw new CheckerException(new Message(message, ErrorCode.VERIFY_ERROR, isReturnSame));
        }
    }

    public static void checkArgument(boolean expr, int errorCode, String message) {
        if (!expr) {
            throw new CheckerException(new Message(message, errorCode));
        }
    }

    public static void checkArgument(boolean expr, String message) {
        if (!expr) {
            throw new CheckerException(new Message(message, ErrorCode.VERIFY_ERROR));
        }
    }

    public static void checkArgument(boolean expr) {
        if (!expr) {
            throw new CheckerException(new Message("", ErrorCode.VERIFY_ERROR));
        }
    }

    public static void checkArgument(boolean expr, int errorCode) {
        checkArgument(expr, errorCode, "");
    }

    public static <T> T checkNotNull(T obj, int errorCode, String message) {
        if (obj == null) {
            throw new CheckerException(new Message(message, errorCode));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, int errorCode, String message, boolean isReturnSame) {
        if (obj == null) {
            throw new CheckerException(new Message(message, errorCode, isReturnSame));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String message, boolean isReturnSame) {
        if (obj == null) {
            throw new CheckerException(new Message(message, ErrorCode.VERIFY_ERROR, isReturnSame));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, String message) {
        if (obj == null) {
            throw new CheckerException(new Message(message, ErrorCode.VERIFY_ERROR));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj) {
        if (obj == null) {
            throw new CheckerException(new Message("", ErrorCode.VERIFY_ERROR));
        }
        return obj;
    }

    public static <T> T checkNotNull(T obj, int errorCode) {
        if (obj == null) {
            throw new CheckerException(new Message("", ErrorCode.VERIFY_ERROR));
        }
        return obj;
    }

    public static void checkState(boolean expr, int errorCode, String message, boolean isReturnSame) {
        if (!expr) {
            throw new CheckerException(new Message(message, errorCode, isReturnSame));
        }
    }

    public static void checkState(boolean expr, String message, boolean isReturnSame) {
        if (!expr) {
            throw new CheckerException(new Message(message, ErrorCode.VERIFY_ERROR, isReturnSame));
        }
    }

    public static void checkState(boolean expr, int errorCode, String message) {
        if (!expr) {
            throw new CheckerException(new Message(message, errorCode));
        }
    }

    public static void checkState(boolean expr, String message) {
        if (!expr) {
            throw new CheckerException(new Message(message, ErrorCode.VERIFY_ERROR));
        }
    }

    public static void checkState(boolean expr) {
        if (!expr) {
            throw new CheckerException(new Message("", ErrorCode.VERIFY_ERROR));
        }
    }

    public static void checkState(boolean expr, int errorCode) {
        if (!expr) {
            throw new CheckerException(new Message("", errorCode));
        }
    }
}
