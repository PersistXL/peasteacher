package com.peas.hsf.websocket;

/**
 * 消息交换处理器
 * Created by duanyihui on 2016/8/12.
 */
public class SimpleExchangeHandler implements ServerMessageHandler.MessageHandler {
    @Override
    public String handleName() {
        return "SimpleExchange";
    }

    @Override
    public void onMessage(HsfWebSocket webSocket, Message message) {
        MessageExchange.exchange(message);
    }
}
