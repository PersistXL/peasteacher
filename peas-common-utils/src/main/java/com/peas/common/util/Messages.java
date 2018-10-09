package com.peas.common.util;

import com.peas.common.base.FileMonitor;
import com.peas.common.base.Resources;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by duanyihui on 2016/8/11.
 */
public class Messages {

    private static ResourceBundle bundle;

    public static void bundle(String message, Locale locale) {
        bundle = ResourceBundle.getBundle(message, locale);
        String filePath = Resources.getResource(message + "_" + locale + ".properties");
        FileMonitor.monitor(filePath, 1, (path) -> {
            ResourceBundle.clearCache();
            bundle = ResourceBundle.getBundle(message, locale);
        });
    }


    public static String getString(String key) {
        String value = bundle.getString(key);
        byte[] bytes = value.getBytes(Charset.forName("ISO-8859-1"));
        return new String(bytes, Charset.forName("utf-8"));
    }

    public static String getFormatString(String key, Object... values) {
        return String.format(getString(key), values);
    }
}
