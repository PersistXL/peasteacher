package com.peas.hsf.http;

/**
 * 异步回调
 *
 * @author dyh 2015年2月3日
 * @see
 * @since 1.0
 */
public interface Callback {

    /**
     * 请求完成回调
     *
     * @param content 响应体
     */
    void handle(Object content);

}
