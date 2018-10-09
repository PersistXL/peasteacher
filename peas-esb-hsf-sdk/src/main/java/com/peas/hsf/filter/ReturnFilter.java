package com.peas.hsf.filter;

import com.peas.hsf.http.ErrorCode;
import com.peas.hsf.http.ResponseEntity;
import com.peas.hsf.model.Message;
import org.glassfish.jersey.server.ContainerResponse;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @Author duanyihui
 * @Date 2016/3/21
 * @Description
 */
@Provider
public class ReturnFilter implements ContainerResponseFilter
{
    /**
     * Filter method called after a response has been provided for a request
     * (either by a {@link } or by a matched resource method. <p> Filters in the
     * filter chain are ordered according to their
     * {@code javax.annotation.Priority} class-level annotation value. </p>
     *
     * @param requestContext  request context.
     * @param responseContext response context.
     * @throws IOException if an I/O exception occurs.
     */
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException
    {
        ContainerResponse response = (ContainerResponse) responseContext;
        if (response.getStatus() == 200 && MediaType.APPLICATION_JSON_TYPE.equals(response.getMediaType()))
        {
            Object object = response.getEntity();
            if (object instanceof Map)
            {
                Map<String, Object> info = (Map<String, Object>) object;
                if (info.containsKey("$type") && info.containsKey("code"))
                {
                    return;
                }
            }
            else if ((object instanceof String))
            {
                String s = Objects.toString(object);
                if (s.startsWith("{") && s.endsWith("}"))
                {
                    return;
                }
            }
            else if (object instanceof Message)
            {
                Message message = (Message) object;
                response.setEntity(message.getMessage());
                return;
            }
            ResponseEntity entity = new ResponseEntity();
            entity.setCode(ErrorCode.SUCCESS);
            entity.setData(object);
            response.setEntity(entity);
        }
    }
}
