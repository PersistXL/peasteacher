package com.peas.hsf.tool;

import com.peas.hsf.http.Client;

/**
 * Created by duan on 2015/9/2.
 */
public class MQProducer {

    private Client client;

    private String uri;

    /**
     * @param client
     * @param uri    {topic name}?type=queue&clientId={id}&timeout=4500
     */
    public MQProducer(Client client, String uri) {
        this.client = client;
        this.uri = uri;
    }

    public void send(String message) {
        client.byFormUnEncoded().post(uri, Forms.newUnEncodedForm().add("message", message));
    }
}
