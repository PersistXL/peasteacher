package com.peas.hsf.http;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.peas.hsf.tool.JsonUtil;
import org.glassfish.grizzly.http.server.ErrorPageGenerator;
import org.glassfish.grizzly.http.server.Request;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author duanyihui
 * @Date 2016/4/5
 * @Description
 */
public class HsfErrorPageGenerator implements ErrorPageGenerator {

    public static final Pattern CHECK_EXCEPTION_REGEX = Pattern.compile(".+com.peas.hsf.exception.CheckerException: Message\\((.+)\\)");

    @Override
    public String generate(Request request, int i, String s, String s1, Throwable exception) {
        Map<String, String> map = Maps.newHashMap();
        map.put("code", i + "");
        map.put("message", s);
        if (exception != null) {
            String message = exception.getMessage();
            Matcher matcher = CHECK_EXCEPTION_REGEX.matcher(message);
            if (matcher.find()) {
                message = matcher.group(1);
                map = Splitter.on(",").omitEmptyStrings().trimResults().withKeyValueSeparator("=").split(message);
                return JsonUtil.toJsonString(map);
            }
        }
        return JsonUtil.toJsonString(map);
    }
}
