package com.peas.hsf.websocket;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by duanyihui on 2016/8/10.
 */
public class WebSocketContext {

    private static Map<String, Map<String, ConnectionInfo>> context = Maps.newConcurrentMap();

    private static Map<String, Map<String, MessageQueue>> messageQueues = Maps.newConcurrentMap();

    private static MessageQueueBuilder queueBuilder;

    public static void initMessageQueueBuilder(String messageBaseDir) {
        queueBuilder = MessageQueueBuilder.create().withBaseDir(messageBaseDir);
    }


    public static void addToContext(ConnectionInfo connectionInfo) {
        Map<String, ConnectionInfo> infos = context.get(connectionInfo.getClientType());
        if (infos == null) {
            infos = Maps.newConcurrentMap();
            context.put(connectionInfo.getClientType(), infos);
        }
        infos.put(connectionInfo.getClientId(), connectionInfo);


    }

    public static HsfWebSocket getWebSocket(String clientType, String clientId) {
        Map<String, ConnectionInfo> infos = context.get(clientType);
        if (infos == null) {
            return null;
        }
        ConnectionInfo connectionInfo = infos.get(clientId);
        if (connectionInfo == null) {
            return null;
        }
        return connectionInfo.getHsfWebSocket();
    }

    public static MessageQueue getMessageQueue(String clientType, String clientId) {
        Map<String, MessageQueue> queues = messageQueues.get(clientType);
        if (queues == null) {
            queues = Maps.newConcurrentMap();
            messageQueues.put(clientType, queues);
        }
        if (!queues.containsKey(clientId)) {
            queues.put(clientId, queueBuilder.build(clientId));
        }
        return queues.get(clientId);
    }


    public static void removeWebSocket(HsfWebSocket webSocket) {
        Map<String, ConnectionInfo> sockets = context.get(webSocket.getBusinessClientType());
        if (sockets != null) {
            sockets.remove(webSocket.getBusinessClientId());
        }
    }
}
