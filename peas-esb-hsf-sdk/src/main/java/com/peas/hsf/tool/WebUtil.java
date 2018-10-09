package com.peas.hsf.tool;

import com.peas.hsf.HsfContext;

import javax.servlet.http.HttpSession;

/**
 * @Author duanyihui
 * @Date 2016/3/18
 * @Description
 */
public final class WebUtil {

    private WebUtil() {
    }


    public static Object getSessionAttribute(HttpSession session, String key) {
        return getSessionAttribute(session, key, Object.class);
    }

    public static <T> T getSessionAttribute(HttpSession session, String key, Class<T> t) {
        if (session != null) {
            Object object = session.getAttribute(key);
            if (object != null) {
                return (T) object;
            }
        }
        return null;
    }

    public static <T> T getSessionAttribute(String key, Class<T> t) {
        return getSessionAttribute(HsfContext.getHttpSession(), key, t);
    }
}
