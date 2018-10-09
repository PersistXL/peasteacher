package com.peas.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * URL 工具
 *
 * @author dyh 2015年5月26日
 * @see
 * @since 1.0
 */
public class UrlUtil {
    private UrlUtil() {
    }

    public static String encode(String url) {
        String s = "";
        try {
            s = URLEncoder.encode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static String decode(String url) {
        String s = "";
        try {
            s = URLDecoder.decode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }
}
