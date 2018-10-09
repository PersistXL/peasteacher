package com.peas.hsf.websocket;

import javax.websocket.MessageHandler;

/**
 * Created by duanyihui on 2016/8/12.
 */
public interface ClientMessageHandler extends MessageHandler {

    void onMessage(WsClients.WsClient client, Message message);
}
