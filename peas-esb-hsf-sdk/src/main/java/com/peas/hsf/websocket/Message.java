package com.peas.hsf.websocket;

import com.peas.hsf.tool.JsonUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by duanyihui on 2016/8/10.
 */
public class Message {

    @Setter
    @Getter
    private String handle;
    @Setter
    @Getter
    private String to;
    @Setter
    @Getter
    private String toType;
    @Setter
    @Getter
    private String from;
    @Setter
    @Getter
    private String fromType;
    @Setter
    @Getter
    private Object content;

    @Getter
    @Setter
    private long timestamp;

    public Message() {
        this.timestamp = System.currentTimeMillis();
    }

    public Message(String handle, String to, String toType, Object content) {
        this.handle = handle;
        this.to = to;
        this.toType = toType;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return JsonUtil.toJsonString(this);
    }

}
