package com.peas.hsf.http.security;

import com.google.common.io.Resources;
import org.glassfish.jersey.SslConfigurator;

import javax.net.ssl.SSLContext;

public class SSLClientContext extends AbstractSSLContext<SSLContext> {

    public SSLClientContext() {
        super();
    }

    public SSLClientContext(String config) {
        super(config);
    }

    @Override
    public SSLContext getContext() {
        SslConfigurator sslc = SslConfigurator.newInstance();
        sslc.trustStoreFile(Resources.getResource(getProperty(SecurityConstant.CLIENT_TRUST_CER)).getPath())
                .trustStorePassword(getProperty(SecurityConstant.CLIENT_TRUST_CER_PWD));
        if (!isOneWayAuthority()) {
            sslc.keyStoreFile(Resources.getResource(getProperty(SecurityConstant.CLIENT_CER)).getPath())
                    .keyPassword(getProperty(SecurityConstant.CLIENT_CER_PWD));
        }
        sslc.securityProtocol(getProperty(SecurityConstant.AUTHORITY_PROTOCOL));
        return sslc.createSSLContext();
    }
}
