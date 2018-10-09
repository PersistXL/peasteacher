package com.peas.hsf.websocket;

import com.peas.hsf.tool.JsonUtil;

import java.nio.charset.Charset;

/**
 * Created by duanyihui on 2016/8/12.
 */
public class MessageExchange {

    public static void exchange(Message message) {
        //尝试发送一次
        HsfWebSocket webSocket = WebSocketContext.getWebSocket(message.getToType(), message.getTo());
        if (webSocket != null && webSocket.isConnected()) {
            //在线
            webSocket.send(JsonUtil.toJsonString(message));
        } else {
            //离线
            MessageQueue queue = WebSocketContext.getMessageQueue(message.getToType(), message.getTo());
            queue.push(JsonUtil.toJsonByteArray(message));
        }
    }

    /**
     * 触发交换
     * PS：目前只实现点对点自动交换 ，定点自动广播消息还未实现
     *
     * @param clientType
     * @param clientId
     */
    public static void fireExchange(String clientType, String clientId) {
        HsfWebSocket webSocket = WebSocketContext.getWebSocket(clientType, clientId);
        if (webSocket != null && webSocket.isConnected()) {
            MessageQueue queue = WebSocketContext.getMessageQueue(clientType, clientId);
            while (webSocket.isConnected() && queue != null && !queue.isEmpty()) {
                byte[] messageBytes = queue.poll();
                webSocket.send(new String(messageBytes, Charset.forName("utf-8")));
            }
        }
    }
}
