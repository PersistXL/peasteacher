package com.peas.hsf;

import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.ContainerResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Author duanyihui
 * @Date 2016/3/17
 * @Description
 */
public final class HsfContext {

    private static final ThreadLocal<ContainerRequest> REQUEST_CONTEXT = new ThreadLocal<>();
    private static final ThreadLocal<ContainerResponse> RESPONSE_CONTEXT = new ThreadLocal<>();
    private static final ThreadLocal<HttpSession> SESSION_CONTEXT = new ThreadLocal<>();
    private static final ThreadLocal<HttpServletRequest> HTTP_REQUEST_CONTEXT = new ThreadLocal<>();
    private static final ThreadLocal<HttpServletResponse> HTTP_RESPONSE_CONTEXT = new ThreadLocal<>();

    private HsfContext() {
    }

    public static void addResponse(ContainerResponse response) {
        RESPONSE_CONTEXT.set(response);
    }

    public static void addRequest(ContainerRequest request) {
        REQUEST_CONTEXT.set(request);
    }

    public static void addHttpRequest(HttpServletRequest request) {
        HTTP_REQUEST_CONTEXT.set(request);
    }

    public static void addHttpResponse(HttpServletResponse response) {
        HTTP_RESPONSE_CONTEXT.set(response);
    }

    public static HttpServletResponse getHttpResponse() {
        return HTTP_RESPONSE_CONTEXT.get();
    }

    public static HttpServletRequest getHttpRequest() {
        return HTTP_REQUEST_CONTEXT.get();
    }

    public static ContainerResponse getResponse() {
        return RESPONSE_CONTEXT.get();
    }

    public static ContainerRequest getRequest() {
        return REQUEST_CONTEXT.get();
    }

    public static void removeResponse() {
        RESPONSE_CONTEXT.remove();
    }

    public static void removeRequest() {
        REQUEST_CONTEXT.remove();
    }

    public static void addHttpSession(HttpSession session) {
        SESSION_CONTEXT.set(session);
    }

    public static HttpSession getHttpSession() {
        return SESSION_CONTEXT.get();
    }

    public static void removeHttpSession() {
        SESSION_CONTEXT.remove();
    }
}
