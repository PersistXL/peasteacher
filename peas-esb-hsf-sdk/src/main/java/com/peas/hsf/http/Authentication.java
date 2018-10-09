package com.peas.hsf.http;

/**
 * 鉴权验证
 *
 * @author dyh
 */
public interface Authentication {

    /**
     * 用户鉴权
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    Client auth(String username, String password);

}
