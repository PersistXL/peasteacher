package com.peas.common.base;

import com.google.common.base.Preconditions;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * 资源辅助类
 *
 * @author dyh
 */
public class Resources {

    private Resources() {
    }

    /**
     * 获取资源
     *
     * @param name 资源名称
     * @return 资源路径
     */
    public static String getResource(String name) {
        URL resource = com.google.common.io.Resources.getResource(name);
        Preconditions.checkState(!Objects.isNullOrEmpty(resource), String.format("resource:[%s] does not exist", name));
        String url = resource.getPath();
        try {
            url = URLDecoder.decode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
        }
        return new File(url).getAbsolutePath();
    }

    public static String getRootPath() {
        return getResource("").replaceAll("\\\\", "/");
    }
}
