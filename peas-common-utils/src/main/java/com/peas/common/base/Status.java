package com.peas.common.base;

import lombok.Getter;
import lombok.ToString;

/**
 * Created by dyh on 2015/6/11.
 */
@ToString
@Getter
public enum Status {
    FAIL("fail", 400),

    SUCCESS("success", 200),

    EXCEPTION("exception", 500);

    private String status;

    private int code;

    private String message;

    private Object resultObject;

    private Status(String status, int code) {
        this.status = status;
        this.code = code;
    }

    public static Status build(boolean isSuccess, String successMessage, String failMessage) {
        return isSuccess ? Status.SUCCESS.message(successMessage) : Status.FAIL.message(failMessage);
    }

    public static Status build(boolean isSuccess) {
        return isSuccess ? Status.SUCCESS : Status.FAIL;
    }

    public Status message(String message) {
        this.message = message;
        return this;
    }

    public Status result(Object resultObject) {
        this.resultObject = resultObject;
        return this;
    }
}
