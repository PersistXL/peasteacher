package com.peas.common.base;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by dyh on 2015/6/30.
 */
public class Paths {

    private Paths() {

    }

    /**
     * 合并路径
     *
     * @param path 路径 顺序 为 路径的深度顺序
     * @return 合并后的路径
     */
    public static String merge(String... path) {
        String fileSep = System.getProperty("file.separator");
        if (Objects.isNullOrEmpty(path)) {
            return fileSep;
        }
        List<String> ps = Lists.newArrayList();
        for (String p : path) {
            ps.addAll(Splitter.on(new CharMatcher() {
                @Override
                public boolean matches(char c) {
                    return c == '/' || c == '\\';
                }
            }).omitEmptyStrings().trimResults().splitToList(p));
        }
        return Joiner.on(fileSep).join(ps);
    }
}
