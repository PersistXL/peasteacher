package com.peas.hsf.provider;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * json组件初始化
 *
 * @author dyh on 2015/2/5.
 */
@Provider
@Produces("application/json")
public class JacksonConfigurator implements ContextResolver<ObjectMapper> {


    /**
     * Get a context of type {@code T} that is applicable to the supplied
     * type.
     *
     * @param type the class of object for which a context is desired
     * @return a context for the supplied type or {@code null} if a
     * context for the supplied type is not available from this provider.
     */
    @Override
    public ObjectMapper getContext(Class<?> type) {
        return HsfJson.mapper;
    }


}
