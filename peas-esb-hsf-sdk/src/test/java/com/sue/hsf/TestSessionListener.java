package com.sue.hsf;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by duanyihui on 2017/5/25.
 */
public class TestSessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        String sessionId = se.getSession().getId();
        System.out.println(sessionId);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
    }
}
