package com.peas.hsf.http;

import javax.ws.rs.core.HttpHeaders;

/**
 * Http 方法，状态，头集合
 *
 * @author dyh 2015年2月3日
 * @see
 * @since 1.0
 */
public interface Http {

    /**
     * HTTP 方法
     *
     * @author dyh 2015年2月3日
     * @see
     * @since 1.0
     */
    interface Method {
        String GET = "GET";

        String POST = "POST";

        String PUT = "PUT";

        String DELETE = "DELETE";

        String HEAD = "HEAD";

        String OPTIONS = "OPTIONS";
    }

    /**
     * 头信息
     *
     * @author dyh 2015年2月3日
     * @see
     * @since 1.0
     */
    interface Headers extends HttpHeaders {

        String APPLICATION_KEY = "Application-Key";

        String APPLICATION_SECURITY_SIGN = "Application-Security-Sign";

        String APPLICATION_AUTHORIZED = "Application-Authorized";
    }

    /**
     * 头信息内容体
     *
     * @author dyh 2015年2月3日
     * @see
     * @since 1.0
     */
    class MediaType extends javax.ws.rs.core.MediaType {
    }
}
