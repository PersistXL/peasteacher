package com.sue.test;


import com.google.common.io.Resources;
import com.peas.hsf.WebContextFramework;
import com.peas.hsf.websocket.SimpleExchangeHandler;
import com.peas.hsf.websocket.WebSocketContext;
import com.peas.hsf.websocket.WebSocketServiceBuilder;

/**
 * Created by peaimage on 2016/8/9.
 */
public class MainServer {
    public static void main(String[] args) {
        WebSocketContext.initMessageQueueBuilder(Resources.getResource("servermsg").getPath());
        WebContextFramework
                .build("http://192.168.0.138", "/service")
                .withWebSocket("/ws", WebSocketServiceBuilder
                        .withMessageHandler(new SimpleExchangeHandler())
                        .build())
                .start();
    }
}
