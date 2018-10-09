package com.peas.common.util;

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * Created by duanyihui on 2017/3/3.
 */
public final class Base64Util
{

    public static String encrypt(byte[] data)
    {
        return Base64.getEncoder().encodeToString(data);
    }

    public static String encrypt(String data)
    {
        return encrypt(data.getBytes(Charset.forName("utf-8")));
    }


    public static String decodeToStr(String data)
    {
        return new String(Base64.getDecoder().decode(data), Charset.forName("utf-8"));
    }

    public static byte[] decode(String data)
    {
        return Base64.getDecoder().decode(data);
    }


}
