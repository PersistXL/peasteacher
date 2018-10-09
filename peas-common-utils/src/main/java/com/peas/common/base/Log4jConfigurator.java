package com.peas.common.base;

import com.google.common.io.Resources;
import org.apache.log4j.PropertyConfigurator;

/**
 * log4j日志初始化
 *
 * @author dyh
 */
public class Log4jConfigurator {
    private Log4jConfigurator() {
    }

    /**
     * 初始化
     *
     * @param path 工程下的相对路径
     */
    public static void configure(String path) {
        PropertyConfigurator.configure(Resources.getResource(path));
    }
}
