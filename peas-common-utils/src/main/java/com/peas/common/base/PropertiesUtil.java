package com.peas.common.base;

import com.google.common.base.Preconditions;
import com.peas.common.base.handler.KeyValueHandler;
import com.peas.common.vo.KeyValuePair;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * 配置文件工具
 *
 * @author dyh
 */
public class PropertiesUtil {

    /**
     * 默认处理器
     */
    private static final KeyValueHandler<String, String> DEFAULT_HANDLER = new KeyValueHandler<String, String>() {
        @Override
        public KeyValuePair<String, String> handler(String key, String value) {
            return new KeyValuePair<String, String>(key.trim(), value.trim());
        }
    };

    /**
     * 私有构造器
     */
    private PropertiesUtil() {
    }

    /**
     * 读取配置文件
     *
     * @param path    工程的classpath下的路径
     * @param handler 键值对处理器
     * @return
     */
    public static Properties read(String path, final KeyValueHandler<String, String> handler) {
        String config = Resources.getResource(path);
        File file = new File(config);
        Preconditions.checkState(file.exists(), String.format("%s : doen't exists", config));
        Properties prop = new Properties();
        try (InputStream in = new FileInputStream(file)) {
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Properties p = new Properties();
        Eachs.on(prop).each(new Eachs.Item<Object, Object>() {
            @Override
            public void item(Object k, Object v) {
                String key = Objects.toString(k, "");
                String value = Objects.toString(v, "");
                KeyValuePair<String, String> kv = handler.handler(key, value);
                p.put(kv.getKey(), kv.getValue());
            }
        });
        return prop;
    }

    /**
     * 读取配置文件
     *
     * @param path 工程的classpath下的路径
     * @return
     */
    public static Properties read(String path) {
        return read(path, DEFAULT_HANDLER);
    }

    public static Properties clone(Properties properties) {
        Properties p = new Properties();
        for (Map.Entry<Object, Object> e : properties.entrySet()) {
            p.put(e.getKey(), e.getValue());
        }
        return p;
    }
}
