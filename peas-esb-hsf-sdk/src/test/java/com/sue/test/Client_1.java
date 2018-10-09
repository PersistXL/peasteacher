package com.sue.test;


import com.google.common.io.Resources;
import com.peas.hsf.websocket.ClientMessageHandler;
import com.peas.hsf.websocket.Message;
import com.peas.hsf.websocket.WsClients;
import com.peas.hsf.websocket.WsClients.WsClient;

/**
 * Created by peaimage on 2016/8/9.
 */
public class Client_1 {
    public static void main(String[] args) throws InterruptedException {
        WsClient wsClient = WsClients.build("ws://192.168.0.138/ws", "123456", "OPC", Resources.getResource("queue").getPath())
                .withHeartbeat(30000)
                .withMessageHandler((ClientMessageHandler) (client, message) -> System.out.println("Client-1 接收:" + message))
                .connect();
        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000);
            wsClient.send(new Message("SimpleExchange", "654321", "OPC", "你好，Client-2 this is message " + i));
        }
        System.out.println("Over send");
    }
}