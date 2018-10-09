package com.peas.hsf.model;

import java.util.Objects;

import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;

import lombok.Data;

/**
 * Created by duan on 2015/8/31.
 */
@Data
public class UnEncodedForm implements Form
{

    private MultivaluedStringMap entry = new MultivaluedStringMap();

    public UnEncodedForm add(String key, String value)
    {
        entry.add(key, value);
        return this;
    }

    public UnEncodedForm add(String key, Object value)
    {
        entry.add(key, Objects.toString(value));
        return this;
    }

    @Override
    public Object getEntry()
    {
        return entry;
    }
}
