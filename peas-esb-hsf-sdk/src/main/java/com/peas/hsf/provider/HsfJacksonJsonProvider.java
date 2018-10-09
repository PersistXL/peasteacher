package com.peas.hsf.provider;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

@Provider
@Consumes({"application/json", "text/json"})
@Produces({"application/json", "text/json"})
public class HsfJacksonJsonProvider extends JacksonJsonProvider {

//    private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

//    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

    @Override
    public ObjectMapper locateMapper(Class<?> type, MediaType mediaType) {
        ObjectMapper mapper = super.locateMapper(type, mediaType);
//        mapper.setDateFormat(sdf);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        return mapper;
    }
}