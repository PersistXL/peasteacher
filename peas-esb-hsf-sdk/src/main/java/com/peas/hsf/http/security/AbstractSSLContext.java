package com.peas.hsf.http.security;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.io.Resources;
import lombok.extern.log4j.Log4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

@Log4j
public abstract class AbstractSSLContext<Context> implements SSLContext<Context> {

    private static final String defaultConfigration = "sslcontext.properties";

    private Properties configration;

    public AbstractSSLContext() {
        this(defaultConfigration);
    }

    public AbstractSSLContext(String configration) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(configration), "configration can not be null or empty");
        this.configration = loadProperties(configration);
    }

    @Override
    public String getProperty(String key) {
        String value = configration.getProperty(key);
        if (Strings.isNullOrEmpty(value)) {
            value = "";
            return value;
        }
        return value.trim();
    }

    @Override
    public boolean isOneWayAuthority() {
        String type = getProperty(SecurityConstant.AUTHORITY_TYPE);
        Preconditions.checkState(type.matches("[1-2]"), "doesn't exists authority type");
        return ONE_WAY_AUTHORITY.equals(type);
    }

    @Override
    public String getAuthorityProtocol() {
        String p = getProperty(SecurityConstant.AUTHORITY_PROTOCOL);
        return Strings.isNullOrEmpty(p) ? "SSL" : p;
    }

    private Properties loadProperties(String configration) {
        URL url = Resources.getResource(configration);
        Preconditions.checkNotNull(url, String.format("[ %s ] resource doen't exsit", configration));
        String path = url.getPath();
        Properties config = new Properties();
        try (InputStream input = new FileInputStream(path)) {
            config.load(input);
        } catch (IOException e) {
            log.error("", e);
        }
        return config;
    }
}
