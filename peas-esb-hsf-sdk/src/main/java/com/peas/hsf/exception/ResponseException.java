package com.peas.hsf.exception;

import lombok.Getter;

import javax.ws.rs.core.Response;

/**
 * 响应异常
 *
 * @author dyh 2015年2月3日
 * @see
 * @since 1.0
 */
public class ResponseException extends RuntimeException {
    private static final long serialVersionUID = -7875759815978222034L;

    /**
     * 响应状态
     */
    @Getter
    private Response.Status status;

    /**
     * 构造器
     *
     * @param status http响应状态
     */
    public ResponseException(Response.Status status) {
        super(String.format("%d - %s - %s", status.getStatusCode(), status.toString().toString(),
                status.getFamily().toString().toLowerCase()));
        this.status = status;
    }
}
