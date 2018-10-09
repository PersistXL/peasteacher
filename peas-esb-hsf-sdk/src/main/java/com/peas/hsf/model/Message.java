package com.peas.hsf.model;

import lombok.Data;

@Data
public class Message {

    private String message;

    private int code;

    /**
     * 消息是否原样输出
     */
    private boolean isReturnSame;


    public Message(String message, int code) {
        this(message, code, false);
    }

    public Message(String message, int code, boolean isReturnSame) {
        this.message = message;
        this.code = code;
        this.isReturnSame = isReturnSame;
    }
}