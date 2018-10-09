package com.peas.hsf.provider;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.peas.hsf.tool.StreamUtil;
import lombok.extern.log4j.Log4j;
import org.glassfish.jersey.server.ContainerRequest;

import javax.ws.rs.client.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * Created by duanyihui on 2017/2/23.
 */
@Log4j
@Provider
@PreMatching
public class HsfHttpProxyProvider implements ContainerRequestFilter {

    private Map<String, String> proxy;

    private String peaCross;

    public HsfHttpProxyProvider() {
        this(null);
    }

    public HsfHttpProxyProvider(String peaCross) {
        proxy = Maps.newHashMap();
        if (!Strings.isNullOrEmpty(peaCross)) {
            try {
                this.peaCross = new String(Base64.getEncoder().encode(peaCross.getBytes("utf-8")), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public HsfHttpProxyProvider addProxyPass(String uri, String to) {
        proxy.put(uri, to);
        return this;
    }

    private String createToLink(ContainerRequest request) {
        UriInfo info = request.getUriInfo();
        URI requestURI = info.getRequestUri();
        String rawPath = requestURI.getRawPath();
        List<String> list = Lists.newArrayList(proxy.keySet());
        list.sort((o1, o2) -> o2.length() - o1.length());
        String key = null;
        for (int i = 0, size = list.size(); i < size; i++) {
            if (rawPath.startsWith(list.get(i))) {
                key = list.get(i);
                break;
            }
        }
        if (key == null) {
            return null;
        }
        String to = proxy.get(key);
        return to + getURI(requestURI.toString(), key);
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        ContainerRequest request = (ContainerRequest) requestContext;
        String link = createToLink(request);
        if (Strings.isNullOrEmpty(link)) {
            return;
        }
        log.info("proxy:" + link);
        MultivaluedMap<String, String> headers = request.getHeaders();
        MultivaluedMap<String, Object> serverHeaders = new MultivaluedHashMap();
        headers.forEach((k, v) -> serverHeaders.putSingle(k, v.get(0)));
        if (!Strings.isNullOrEmpty(peaCross)) {
            serverHeaders.putSingle("PEA-CROSS", peaCross);
        }
        Map<String, Cookie> cookies = requestContext.getCookies();
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(link);
        Invocation.Builder builder = target.request().headers(serverHeaders);
        cookies.values().forEach(cookie -> builder.cookie(cookie));
        request.getPropertyNames().forEach(name -> builder.property(name, request.getProperty(name)));
        Invocation invocation = null;
        switch (request.getMethod()) {
            case "DELETE":
            case "GET": {
                invocation = builder.build(request.getMethod(), null);
                break;
            }
            case "POST":
            case "PUT": {
                InputStream stream = request.getEntityStream();
                byte[] entity = StreamUtil.read(stream);
                Entity<?> entity1 = Entity.entity(entity, request.getMediaType());
                invocation = builder.build(request.getMethod(), entity1);
                break;
            }
        }
        Response response = invocation.invoke();
        MultivaluedHashMap<String,Object> map=new MultivaluedHashMap<>();
        map.add("Content-Type",  response.getHeaderString("Content-Type"));
        request.abortWith(Response.fromResponse(response).replaceAll(map).build());
    }

    private String getURI(String requestUri, String contextName) {
        int index = requestUri.indexOf(contextName);
        return requestUri.substring(index + contextName.length());
    }
}
