package com.sue.hsf;

import com.google.common.collect.Sets;
import com.peas.hsf.Application;
import com.peas.hsf.WebContextFramework;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.jersey.message.internal.StreamingOutputProvider;

import javax.servlet.SessionTrackingMode;

/**
 * Created by duanyihui on 2017/5/25.
 */
public class TestHsfApplication {

    public static void main(String[] args) {

        WebContextFramework
                .build("http://0.0.0.0:9999", "/")
                .withHandler(new WebContextFramework.Handler() {
                    @Override
                    public void handle(HttpServer server, WebappContext webappContext) {
                        webappContext.setSessionTrackingModes(Sets.newHashSet(SessionTrackingMode.URL,
                                SessionTrackingMode.COOKIE));
                        webappContext.addListener(TestSessionListener.class);
                    }
                })
                .addResource("hsf", new Application() {
                    @Override
                    protected void registerClass() {
                        register(TestResource.class);
                    }
                })
                .start();

    }
}
