package com.peas.hsf.websocket;

import lombok.Getter;

/**
 * 关闭原因
 * Created by duanyihui on 2016/8/10.
 */
public enum WsReturnCode {

    AUTH_SUCCESS(200, "认证成功"),

    BAD_REQUEST(4000, "请求不正确，缺少必要参数"),

    DUPLICATE_CONNECTIONS(4001, "重复链接"),

    BLACKLIST(4002, "黑名单");


    @Getter
    private int code;

    @Getter
    private String reason;

    WsReturnCode(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }
}
