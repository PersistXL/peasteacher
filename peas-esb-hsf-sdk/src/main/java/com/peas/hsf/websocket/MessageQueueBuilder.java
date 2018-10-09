package com.peas.hsf.websocket;

import net.apexes.fqueue.exception.FileFormatException;

import java.io.IOException;

/**
 * Created by duanyihui on 2016/8/15.
 */
public class MessageQueueBuilder {


    private String basePath;

    public static MessageQueueBuilder create() {
        return new MessageQueueBuilder();
    }

    public MessageQueueBuilder withBaseDir(String dir) {
        this.basePath = dir;
        return this;
    }

    public MessageQueue build(String queueName) {
        try {
            return new MessageQueue(basePath, queueName, 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FileFormatException e) {
            e.printStackTrace();
        }
        return null;
    }
}
