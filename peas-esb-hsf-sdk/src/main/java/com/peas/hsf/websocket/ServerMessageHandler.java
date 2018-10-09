package com.peas.hsf.websocket;

/**
 * Created by duanyihui on 2016/8/10.
 */
public interface ServerMessageHandler {

    String handleName();

    interface TextHandler extends ServerMessageHandler {
        void onMessage(HsfWebSocket webSocket, String string);
    }

    interface MessageHandler extends ServerMessageHandler {
        void onMessage(HsfWebSocket webSocket, Message message);
    }

    interface ObjectHandler extends ServerMessageHandler {
        void onMessage(HsfWebSocket webSocket, byte[] bytes);
    }
}
