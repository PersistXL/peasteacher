package com.peas.hsf.http.security;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.ws.rs.ext.Provider;

@Provider
public class PeasHostnameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession session) {
        // return "127.0.0.1".equals(hostname) || "localhost".equals(hostname);
        return true;
    }
}
