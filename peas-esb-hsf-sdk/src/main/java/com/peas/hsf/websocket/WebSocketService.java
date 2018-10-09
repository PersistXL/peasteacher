package com.peas.hsf.websocket;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Bytes;
import com.google.gson.reflect.TypeToken;
import com.peas.hsf.tool.GsonUtil;
import com.peas.hsf.tool.JsonUtil;
import lombok.Setter;
import org.glassfish.grizzly.http.HttpRequestPacket;
import org.glassfish.grizzly.websockets.*;

import java.util.List;
import java.util.Map;

import static com.peas.hsf.websocket.WsReturnCode.AUTH_SUCCESS;

/**
 * WebSocket服务
 * Created by duanyihui on 2016/8/9.
 */
public class WebSocketService extends WebSocketApplication {

    @Setter
    private AuthenticationHandler authenticationHandler;

    private Map<String, List<ServerMessageHandler>> handlerList;

    WebSocketService() {
        authenticationHandler = requestPacket -> AUTH_SUCCESS;
        handlerList = Maps.newHashMap();
    }

    public WebSocketService withMessageHandler(ServerMessageHandler... handlers) {
        Lists.newArrayList(handlers).forEach((handler -> {
            String handleName = handler.handleName();
            List<ServerMessageHandler> messageHandlers = handlerList.get(handleName);
            if (messageHandlers == null) {
                messageHandlers = Lists.newArrayList();
                handlerList.put(handleName, messageHandlers);
            }
            messageHandlers.add(handler);
        }));
        return this;
    }

    @Override
    public WebSocket createSocket(ProtocolHandler handler, HttpRequestPacket requestPacket, WebSocketListener... listeners) {
        String query = requestPacket.getQueryString();
        if (Strings.isNullOrEmpty(query)) {
            handler.close(WsReturnCode.BAD_REQUEST.getCode(), WsReturnCode.BAD_REQUEST.getReason());
            return null;
        }
        Map<String, String> arguments = Splitter.on("&").trimResults().omitEmptyStrings().withKeyValueSeparator("=").split(query);
        String clientId = arguments.get("clientId");
        String clientType = arguments.get("clientType");
        if (Strings.isNullOrEmpty(clientId) || Strings.isNullOrEmpty(clientType)) {
            handler.close(WsReturnCode.BAD_REQUEST.getCode(), WsReturnCode.BAD_REQUEST.getReason());
            return null;
        }

        WsReturnCode code = authenticationHandler.authenticate(requestPacket);
        if (code.equals(AUTH_SUCCESS)) {
            HsfWebSocket socket = new HsfWebSocket(handler, requestPacket, listeners);
            socket.setBusinessClientId(clientId);
            socket.setBusinessClientType(clientType);
            ConnectionInfo connectionInfo = new ConnectionInfo(clientId, clientType, requestPacket.getRemoteAddress(), requestPacket.getRemotePort());
            connectionInfo.setHsfWebSocket(socket);
            WebSocketContext.addToContext(connectionInfo);
            return socket;
        } else {
            handler.close(code.getCode(), code.getReason());
            return null;
        }
    }

    @Override
    public void onClose(WebSocket socket, DataFrame frame) {
        super.onClose(socket, frame);
    }

    @Override
    public void onConnect(WebSocket socket) {
        super.onConnect(socket);
        HsfWebSocket hsfSocket = (HsfWebSocket) socket;
        MessageExchange.fireExchange(hsfSocket.getBusinessClientType(),
                hsfSocket.getBusinessClientId());
    }

    @Override
    public boolean remove(WebSocket socket) {
        WebSocketContext.removeWebSocket((HsfWebSocket) socket);
        return super.remove(socket);
    }

    @Override
    protected boolean onError(WebSocket webSocket, Throwable t) {
        return super.onError(webSocket, t);
    }

    @Override
    public void onMessage(WebSocket socket, String text) {
        try {
            Message message = GsonUtil.parse(text, new TypeToken<Message>() {
            });
            if (handlerList != null) {
                List<ServerMessageHandler> list = handlerList.get(message.getHandle());
                if (list != null) {
                    list.stream().forEach(handler -> {
                        if (handler instanceof ServerMessageHandler.MessageHandler) {
                            ((ServerMessageHandler.MessageHandler) handler).onMessage((HsfWebSocket) socket, message);
                        }
                    });
                } else {
                    socket.send(createServerMessage("has no handle"));
                }
            }
        } catch (Exception e) {
            socket.send(createServerMessage(e.getMessage()));
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(WebSocket socket, byte[] bytes) {

//        try {
//            if (handlerList != null) {
//                List<ServerMessageHandler> list = handlerList.get(message.getHandle());
//                if (list != null) {
//                    list.stream().forEach(handler -> {
//                        if (handler instanceof ServerMessageHandler.MessageHandler) {
//                            ((ServerMessageHandler.MessageHandler) handler).onMessage((HsfWebSocket) socket, message);
//                        }
//                    });
//                } else {
//                    socket.send(createServerMessage("has no handle"));
//                }
//            }
//        } catch (Exception e) {
//            socket.send(createServerMessage(e.getMessage()));
//            e.printStackTrace();
//        }
    }

    private String createServerMessage(String content) {
        Message message = new Message(null, "", "SYSTEM", content);
        message.setFrom("-1");
        message.setFromType("SYSTEM");
        return JsonUtil.toJsonString(message);
    }

    @Override
    public void onPing(WebSocket socket, byte[] bytes) {
        socket.sendPong(new byte[]{10});
    }

    @Override
    public void onPong(WebSocket socket, byte[] bytes) {
        socket.sendPing(new byte[]{9});
    }
}
