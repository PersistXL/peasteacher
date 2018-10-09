package com.peas.hsf.tool;

import com.google.common.base.Preconditions;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.Objects;

/**
 * Created by duanyihui on 2017/3/24.
 */
public class ReturnTool
{
    public static String getString(Object value)
    {
        return Objects.toString(value);
    }

    public static byte[] getBytes(Object value)
    {
        Preconditions.checkState((value instanceof byte[]), "非byte[]数据");
        return (byte[]) value;
    }

    public static Response getResponse(Object value)
    {
        Preconditions.checkState(value instanceof Response, "非Response返回值");
        return (Response) value;
    }

    public static <T> T getValue(Object value, Class<T> tClass)
    {
        Response response = getResponse(value);
        T r = response.readEntity(tClass);
        response.close();
        return r;
    }

    public static <T> T getValue(Object value, GenericType<T> genericType)
    {
        Response response = getResponse(value);
        T r = response.readEntity(genericType);
        response.close();
        return r;
    }

    public static boolean isTrue(String v)
    {
        return v == null ? false : v.toLowerCase().matches("true");
    }
}
