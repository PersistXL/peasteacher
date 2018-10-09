package com.peas.hsf.websocket;

import lombok.Getter;
import lombok.Setter;
import org.glassfish.grizzly.http.HttpRequestPacket;
import org.glassfish.grizzly.websockets.DefaultWebSocket;
import org.glassfish.grizzly.websockets.ProtocolHandler;
import org.glassfish.grizzly.websockets.WebSocketListener;

/**
 * Created by duanyihui on 2016/8/10.
 */
public class HsfWebSocket extends DefaultWebSocket {

    @Setter
    @Getter
    private String businessClientId;

    @Setter
    @Getter
    private String businessClientType;

    @Getter
    private ProtocolHandler protocolHandler;
    @Getter
    private HttpRequestPacket request;
    @Getter
    private WebSocketListener[] WebSocketListener;


    public HsfWebSocket(ProtocolHandler protocolHandler, HttpRequestPacket request, WebSocketListener... listeners) {
        super(protocolHandler, request, listeners);
        this.protocolHandler = protocolHandler;
        this.request = request;
        this.WebSocketListener = listeners;
    }
}
