package com.sue.proxy;

import com.google.common.collect.Lists;
import org.glassfish.jersey.server.ContainerRequest;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by duanyihui on 2017/2/23.
 */

public class TestProxy
{

    private static Map<String, String> proxy = new HashMap<String, String>()
    {
        {
            put("/manager/qc", "http://127.0.0.1:8080/qc");
            put("/manager/qc/apply_form", "http://127.0.0.1:8080/workflow/apply_form");
        }
    };

    public static void main(String[] args)
    {
        ContainerRequest request = new ContainerRequest(URI.create("http://192.168.0.1"), URI.create("/manager"), "POST", null, null);
        createToLink(request);
    }

    private static String createToLink(ContainerRequest request)
    {
        UriInfo info = request.getUriInfo();
        URI requestURI = info.getRequestUri();
        String rawPath = requestURI.getRawPath();
        List<String> list = Lists.newArrayList(proxy.keySet());
        list.sort((o1, o2) -> o2.length() - o1.length());
        String key = null;
        for (int i = 0, size = list.size(); i < size; i++)
        {
            if (rawPath.startsWith(list.get(i)))
            {
                key = list.get(i);
                break;
            }
        }
        if (key == null)
        {
            return null;
        }
        String to = proxy.get(key);
        return to + getURI(requestURI.toString(), key);
    }

    private static String getURI(String requestUri, String contextName)
    {
        int index = requestUri.indexOf(contextName);
        return requestUri.substring(index + contextName.length());
    }

}
