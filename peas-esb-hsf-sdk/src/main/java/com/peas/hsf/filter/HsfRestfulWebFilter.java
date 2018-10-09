package com.peas.hsf.filter;

import com.peas.hsf.HsfContext;
import org.glassfish.jersey.server.ContainerRequest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * @Author duanyihui
 * @Date 2016/3/17
 * @Description
 */
@Provider
public class HsfRestfulWebFilter implements ContainerRequestFilter, ContainerResponseFilter {


    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        ContainerRequest request = (ContainerRequest) requestContext.getRequest();
        HsfContext.addRequest(request);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        HsfContext.removeRequest();
        HsfContext.removeResponse();
    }
}
