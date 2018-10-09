package com.peas.hsf.http.security;

/**
 * 鉴权验证
 *
 * @author dyh
 */
public interface AuthenticationVerifier {
    /**
     * 验证
     *
     * @param username 用户名
     * @param password 密码
     * @return 是否通过
     */
    boolean verify(String username, String password);
}
