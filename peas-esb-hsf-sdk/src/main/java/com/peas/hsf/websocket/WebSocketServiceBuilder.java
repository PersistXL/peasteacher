package com.peas.hsf.websocket;

/**
 * Created by duanyihui on 2016/8/10.
 */
public class WebSocketServiceBuilder {

    public static Builder withMessageHandler(ServerMessageHandler... handler) {
        return new Builder(handler);
    }


    public static class Builder {

        private ServerMessageHandler[] handlers;

        public Builder(ServerMessageHandler... handlers) {
            this.handlers = handlers;
        }

        public WebSocketService build() {
            return new WebSocketService().withMessageHandler(handlers);
        }
    }
}
