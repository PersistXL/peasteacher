package com.peas.hsf.websocket;

import net.apexes.fqueue.FQueue;
import net.apexes.fqueue.exception.FileFormatException;

import java.io.File;
import java.io.IOException;
import java.util.Queue;

/**
 * Created by duanyihui on 2016/8/10.
 */
public class MessageQueue {

    private Queue<byte[]> queue;

    private String baseDir;
    private String queueName;
    private Integer num;

    public MessageQueue(String baseDir, String queueName, Integer num) throws IOException, FileFormatException {
        this.baseDir = baseDir;
        this.queueName = queueName;
        this.num = num == null ? 2097152 : num;
        String path = this.baseDir + "/" + this.queueName;
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        queue = new FQueue(path, this.num);
    }

    public void push(byte[] bytes) {
        queue.add(bytes);
    }

    public byte[] poll() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
