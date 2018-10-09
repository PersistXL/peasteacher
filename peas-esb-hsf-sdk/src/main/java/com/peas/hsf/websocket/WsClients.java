package com.peas.hsf.websocket;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.gson.reflect.TypeToken;
import com.peas.hsf.common.Functions;
import com.peas.hsf.tool.DateUtils;
import com.peas.hsf.tool.GsonUtil;
import com.peas.hsf.tool.JsonUtil;
import lombok.extern.log4j.Log4j;
import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by duanyihui on 2016/8/10.
 */
@Log4j
public class WsClients {

    public static WsClient build(String uri, String clientId, String clientType, String messagePath) {
        if (uri.indexOf("?") != -1) {
            uri = String.format("%s&clientId=%s&clientType=%s", uri, clientId, clientType);
        } else {
            uri = String.format("%s?clientId=%s&clientType=%s", uri, clientId, clientType);
        }
        return new WsClient(uri, clientId, clientType, messagePath);
    }

    public static class WsClient {

        private MessageQueue messages;

        private ClientManager manager;

        private Session session;

        private String uri;

        private List<ClientMessageHandler> messageHandlers;

        private final String clientId;

        private final String clientType;

        private long heartbeat = -1;

        private WsClient self;

        public WsClient(String uri, String clientId, String clientType, String messagePath) {
            this.self = this;
            this.uri = uri;
            this.clientId = clientId;
            this.clientType = clientType;
            this.manager = ClientManager.createClient();
            this.messages = MessageQueueBuilder.create().withBaseDir(messagePath).build(clientId);
            Executors.newSingleThreadExecutor().execute(() -> {
                while (true) {
                    while (!messages.isEmpty()) {
                        if (session != null && session.isOpen()) {
                            try {
                                session.getBasicRemote().sendText(new String(messages.poll(), "utf-8"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            sleep(500);
                        }
                    }
                    sleep(1000);
                }
            });
        }

        public WsClient withMessageHandler(ClientMessageHandler... handlers) {
            messageHandlers = Lists.newArrayList(handlers);
            return this;
        }

        public WsClient withHeartbeat(long s) {
            Preconditions.checkState(s >= 0);
            heartbeat = s;
            return this;
        }

        public void send(Message message) {
            Preconditions.checkNotNull(this.session, "连接未建立");
            //入队
            message.setFrom(clientId);
            message.setFromType(clientType);
            this.messages.push(JsonUtil.toJsonByteArray(message));
        }

        public WsClient connect() {
            int i = 0;
            while (true) {
                log.debug("connect [" + uri + "] " + (i++ + 1) + " time..." + " at " + DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
                try {
                    this.session = manager.connectToServer(new Endpoint() {
                        private ScheduledExecutorService service;

                        @Override
                        public void onOpen(Session session, EndpointConfig config) {
                            session.addMessageHandler(new MessageHandler.Whole<String>() {
                                @Override
                                public void onMessage(String messageText) {
                                    Message message = GsonUtil.parse(messageText, new TypeToken<Message>() {
                                    });
                                    Functions.doIf(messageHandlers != null, () -> messageHandlers.stream()
                                            .forEach(messageHandler -> messageHandler.onMessage(self, message)));
                                }
                            });
                            if (!(heartbeat < 0)) {
                                service = Executors.newScheduledThreadPool(1);
                                service.scheduleAtFixedRate(() -> {
                                    try {
                                        log.debug("heartbeat at:" + DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
                                        session.getBasicRemote().sendPing(ByteBuffer.allocate(9));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }, 0, heartbeat, TimeUnit.MILLISECONDS);
                            }
                        }

                        @Override
                        public void onClose(Session session, CloseReason closeReason) {
                            log.debug("CLOSE:" + session + "  " + closeReason);
                            reConnect(session);
                        }

                        @Override
                        public void onError(Session session, Throwable thr) {
                            log.error("ERROR:" + session, thr);
                            reConnect(session);
                        }

                        private void reConnect(Session session) {
                            try {
                                service.shutdownNow();
                                session.close();
                                //重新连接
                                connect();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, URI.create(uri));
                    break;
                } catch (Exception e) {
                    sleep(60000);
                }
            }
            if (this.session == null) {
                Throwables.propagate(new TimeoutException("连接 " + uri + " 超时"));
            } else {
                log.debug("WebSocket connected");
            }
            return this;
        }

    }

    private static void sleep(long s) {
        try {
            Thread.sleep(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
