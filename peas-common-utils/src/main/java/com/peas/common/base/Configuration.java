package com.peas.common.base;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.peas.common.util.Json;
import com.peas.common.util.MD5Util;
import org.apache.commons.collections.set.UnmodifiableSet;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 初始文件
 * Created by dyh on 2015/6/5.
 */
public class Configuration {

    public static String BUSLINE = "com.peas.common.base.Configuration";
    public static String EVENT_CHANGE = "hasChanged";

    private static boolean hasBudled = false;
    private static final String DEFAULT_VIEW = "global";

    private static final String BOM = "\uFEFF";

    /**
     * 配置文件路径
     */
    private static String configPath;

    private static Map<String, Map<String, Object>> configuration = Maps.newConcurrentMap();


    private static void init() {
        synchronized (configuration) {
            try {
                List<String> lines = Files.readLines(new File(configPath), Charset.forName("utf-8"));
                if (lines.size() > 1) {
                    String firstLine = Objects.toString(lines.remove(0)).replace(BOM, "");
                    lines.add(0, firstLine);
                }
                String sysKey = DEFAULT_VIEW;
                for (String line : lines) {
                    line = Objects.toString(line).trim();
                    if (Objects.isNullOrEmpty(line) || line.startsWith("#")) {
                        continue;
                    }
                    if (line.matches("^<\\S+>$")) {
                        line = line.substring(1, line.length() - 1).trim();
                        configuration.put(line, new HashMap<>());
                        sysKey = line;
                        continue;
                    }
                    int index = line.indexOf("=");
                    if (index == -1) {
                        continue;
                    }
                    String key = line.substring(0, index).trim();
                    String value = "";
                    if (index + 1 < line.length()) {
                        value = line.substring(index + 1, line.length()).trim();
                    }
                    Map<String, Object> c = configuration.get(sysKey);
                    if (c == null) {
                        c = new HashMap<>();
                        configuration.put(sysKey, c);
                    }
                    c.put(key, value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getProperty(String key) {
        return getProperty(DEFAULT_VIEW, key);
    }

    public static String getProperty(String view, String key) {
        Map<String, Object> m = configuration.get(view);
        if (Objects.isNullOrEmpty(m)) {
            return "";
        }
        return Objects.toString(m.get(key));
    }

    public static Map<String, String> getMapProperty(String view, String key) {
        if (!configuration.containsKey(view)) {
            return null;
        }
        if (!configuration.get(view).containsKey(key)) {
            return null;
        }
        return (Map<String, String>) configuration.get(view).get(key);
    }

    public static Map<String, String> getMapProperty(String key) {
        return getMapProperty(DEFAULT_VIEW, key);
    }

    public static List<String> getListProperty(String view, String key) {
        if (!configuration.containsKey(view)) {
            return null;
        }
        if (!configuration.get(view).containsKey(key)) {
            return null;
        }
        return (List<String>) Json.deserialize(Objects.toString(configuration.get(view).get(key)), List.class);
    }

    public static List<String> getListProperty(String key) {
        return getListProperty(DEFAULT_VIEW, key);
    }

    public static String toStringContent() {
        return configuration.toString();
    }

    /**
     * 转换为Properties
     *
     * @param view
     * @return
     */
    public static Properties transform(String view) {
        Map<String, Object> viewMap = configuration.get(view);
        if (viewMap == null) {
            return new Properties();
        }
        Properties properties = new Properties();
        for (Map.Entry<String, Object> e : viewMap.entrySet()) {
            String value = Objects.toString(e.getValue());
            if (Strings.isNullOrEmpty(value)) {
                continue;
            }
            properties.setProperty(e.getKey(), value);
        }
        return properties;
    }

    public static Set<String> viewSet() {
        return UnmodifiableSet.decorate(configuration.keySet());
    }

    /**
     * 配置文件绑定
     *
     * @param configPath 配置文件路径
     * @return
     */
    public static void bundle(String configPath) {
        if (hasBudled) {
            return;
        }
        Preconditions.checkArgument(!Objects.isNullOrEmpty(configPath), "configuration can not be null");
        Configuration.configPath = Resources.getResource(configPath);
        init();
        monitor();
        hasBudled = true;
    }

    /**
     * 配置文件监控 提供近似实时的更新能力
     */
    private static void monitor() {
        String[] lastMd5 = new String[]{"1"};
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate((Runnable) () -> {
            try {
                byte[] bytes = java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(configPath));
                String md5 = MD5Util.sign(bytes);
                if (!lastMd5[0].equals(md5)) {
                    lastMd5[0] = md5;
                    init();
                    EventBusCenter.post(BUSLINE, Events.newEvent(EVENT_CHANGE, "", ""));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 5, 5, TimeUnit.SECONDS);
    }

}
