package com.peas.hsf.model;

import java.util.Objects;

import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;

/**
 * Created by duan on 2015/8/31.
 */
public class DataMultiPartForm implements Form
{

    private FormDataMultiPart entry = new FormDataMultiPart();

    public DataMultiPartForm add(String key, String value)
    {
        entry.field(key, value);
        return this;
    }

    public DataMultiPartForm add(String key, Object value)
    {
        entry.field(key, Objects.toString(value));
        return this;
    }

    public DataMultiPartForm add(String key, Object value, MediaType mediaType)
    {
        // FormDataBodyPart part = new FormDataBodyPart();
        // part.setValue(mediaType, value);
        entry.field(key, value, mediaType);
        return this;
    }

    @Override
    public Object getEntry()
    {
        return entry;
    }
}
