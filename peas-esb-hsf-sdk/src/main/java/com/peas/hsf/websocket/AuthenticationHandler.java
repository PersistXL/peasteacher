package com.peas.hsf.websocket;

import org.glassfish.grizzly.http.HttpRequestPacket;

/**
 * Created by duanyihui on 2016/8/10.
 */
public interface AuthenticationHandler {

    WsReturnCode authenticate(HttpRequestPacket requestPacket);

}
