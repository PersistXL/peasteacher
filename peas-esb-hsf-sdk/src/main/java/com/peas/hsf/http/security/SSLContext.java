package com.peas.hsf.http.security;

/**
 * SSL认证容器
 *
 * @author dyh
 */
public interface SSLContext<Context> {
    /**
     * 单向认证
     */
    String ONE_WAY_AUTHORITY = "1";

    /**
     * 双向认证
     */
    String TWO_WAY_AUTHORITY = "2";

    /**
     * 是否为单向认证（即 服务器身份证明）
     *
     * @return
     */
    boolean isOneWayAuthority();

    /**
     * 获取认证协议
     *
     * @return 协议
     */
    String getAuthorityProtocol();

    /**
     * 获取属性
     *
     * @param key
     * @return
     */
    String getProperty(String key);

    /**
     * 获取容器
     *
     * @return
     */
    Context getContext();

}
