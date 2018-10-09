package com.peas.hsf.http.security;

import com.google.common.io.Resources;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;

/**
 * 服务器端SSL Context
 *
 * @author dyh
 */
public class SSLServerContext extends AbstractSSLContext<SSLEngineConfigurator> {

    public SSLServerContext() {
        super();
    }

    public SSLServerContext(String config) {
        super(config);
    }

    @Override
    public SSLEngineConfigurator getContext() {
        SSLContextConfigurator sslContext = new SSLContextConfigurator();
        sslContext.setKeyStoreFile(Resources.getResource(getProperty(SecurityConstant.SERVER_CER)).getPath());
        sslContext.setKeyStorePass(getProperty(SecurityConstant.SERVER_CER_PWD));
        if (!isOneWayAuthority()) {
            sslContext.setTrustStoreFile(Resources.getResource(getProperty(SecurityConstant.SERVER_TRUST_CER))
                    .getPath());
            sslContext.setTrustStorePass(getProperty(SecurityConstant.SERVER_TRUST_CER_PWD));
        }
        sslContext.setSecurityProtocol(getAuthorityProtocol());
        return new SSLEngineConfigurator(sslContext).setClientMode(false).setWantClientAuth(isOneWayAuthority())
                .setNeedClientAuth(!isOneWayAuthority());
    }

}
