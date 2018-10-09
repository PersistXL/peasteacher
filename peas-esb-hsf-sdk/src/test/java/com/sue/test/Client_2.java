package com.sue.test;

import com.google.common.io.Resources;
import com.peas.hsf.websocket.ClientMessageHandler;
import com.peas.hsf.websocket.Message;
import com.peas.hsf.websocket.WsClients;

/**
 * Created by duanyihui on 2016/8/12.
 */
public class Client_2 {
    public static void main(String[] args) {
        WsClients.WsClient wsClient = WsClients.build("ws://192.168.0.138/ws", "654321", "OPC", Resources.getResource("queue").getPath())
                .withHeartbeat(30000)
                .withMessageHandler((ClientMessageHandler) (client, message) -> {
                    System.out.println("Client-2接收:" + message);
//                    if (!"SYSTEM".equals(message.getFromType())) {
//                        client.send(new Message("SimpleExchange", "123456", "OPC", "2 说我收到了：" + message.getContent()));
//                    }
                })
                .connect();
        wsClient.send(new Message("SimpleExchange", "123456", "OPC", "你好，Client-1"));
    }
}
