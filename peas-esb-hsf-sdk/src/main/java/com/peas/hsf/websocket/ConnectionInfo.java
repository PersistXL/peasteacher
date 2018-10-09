package com.peas.hsf.websocket;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by duanyihui on 2016/8/10.
 */

@Setter
@Getter
public class ConnectionInfo {

    private String clientId;

    private String clientType;

    private String remoteAddress;

    private Integer remotePort;

    private HsfWebSocket hsfWebSocket;

    public ConnectionInfo(String clientId, String clientType, String remoteAddress, Integer remotePort) {
        this.clientId = clientId;
        this.clientType = clientType;
        this.remoteAddress = remoteAddress;
        this.remotePort = remotePort;
    }

}
