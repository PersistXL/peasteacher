package com.peas.hsf.http.security;

/**
 * 安全协议常量
 *
 * @author dyh
 */
public interface SecurityConstant {
    /**
     * 认证方式
     */
    String AUTHORITY_TYPE = "https.security.authority.type";

    /**
     * 认证协议
     */
    String AUTHORITY_PROTOCOL = "https.security.authority.protocol";

    /**
     * 服务器端证书
     */
    String SERVER_CER = "https.security.server.cert";

    /**
     * 服务器证书密码
     */
    String SERVER_CER_PWD = "https.security.server.storepass";

    /**
     * 服务器密钥
     */
    String SERVER_KEY_PWD = "https.security.server.keypass";

    /**
     * 服务器受信证书
     */
    String SERVER_TRUST_CER = "https.security.server.trust.store";

    /**
     * 服务器受信证书密钥
     */
    String SERVER_TRUST_CER_PWD = "https.security.server.trust.store.pass";

    /**
     * 客户端证书
     */
    String CLIENT_CER = "https.security.client.cert";

    /**
     * 客户端证书密钥
     */
    String CLIENT_CER_PWD = "https.security.client.storepass";

    /**
     * 客户端密钥
     */
    String CLIENT_KEY_PWD = "https.security.client.keypass";

    /**
     * 客户端受信证书
     */
    String CLIENT_TRUST_CER = "https.security.client.trust.store";

    /**
     * 客户端受信证书密钥
     */
    String CLIENT_TRUST_CER_PWD = "https.security.client.trust.store.pass";

}
