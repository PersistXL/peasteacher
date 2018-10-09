package com.peas.hsf.tool;

import com.google.common.collect.Lists;
import com.peas.hsf.http.Client;
import lombok.extern.log4j.Log4j;

import java.util.List;

/**
 * Created by duan on 2015/9/2.
 */
@Log4j
public class MQConsumer {

    private List<MessageListener> listeners = Lists.newArrayList();

    private Client client;

    private String uri;

    private boolean alive = true;

    private long activeTime = 0;

    private long timeout;

    private boolean isAutoClose = true;

    /**
     * /mq/{topic name}?type=queue&clientId={id}&timeout=4500
     *
     * @param client
     * @param uri
     */
    public MQConsumer(Client client, String uri) {
        this(client, uri, true, 1000 * 60 * 30);
    }

    public MQConsumer(Client client, String uri, long timeout) {
        this(client, uri, true, timeout);
    }

    public MQConsumer(Client client, String uri, boolean isAutoClose, long timeout) {
        this.client = client;
        this.uri = uri;
        this.isAutoClose = isAutoClose;
        this.timeout = timeout;
    }

    public MQConsumer addListener(MessageListener listener) {
        listeners.add(listener);
        return this;
    }

    public void receive() {
        Thread thread = new Thread(() -> {
            while (alive) {
                try {
                    Object object = client.get(uri);
                    if (object != null && !"".equals(object)) {
                        activeTime = System.currentTimeMillis();
                        execute(object);
                    } else if (isAutoClose && ((System.currentTimeMillis() - activeTime) > timeout)) {
                        alive = false;
                        log.info(Thread.currentThread().getName() + "---> closed");
                    }
                } catch (Exception e) {
                    log.error("", e);
                }
            }
        });
        thread.setName(String.format("MQConsumer [ %s ]", uri));
        activeTime = System.currentTimeMillis();
        thread.start();
    }

    public void stopRecevice() {
        this.alive = false;
    }

    private void execute(Object object) {
        for (MessageListener listener : listeners) {
            if (object instanceof String) {
                listener.onTextMessage((String) object);
            }
        }
    }


    public static interface MessageListener {
        void onTextMessage(String message);
    }
}
