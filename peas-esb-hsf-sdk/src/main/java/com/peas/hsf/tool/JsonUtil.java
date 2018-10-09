package com.peas.hsf.tool;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Json工具 服务于Jersey的Json转换
 *
 * @author dyh 2015年2月3日
 * @see
 * @since 1.0
 */
public class JsonUtil {
    /**
     * 私有构造器
     */
    private JsonUtil() {
    }

    /**
     * 转换为JSON字符串
     *
     * @param object 对象
     * @return JSON串
     */
    public static String toJsonString(Object object) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String jsonString = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            JsonFactory jf = mapper.getFactory();
            JsonGenerator jsonGenerator = jf.createGenerator(out);
            jsonGenerator.writeObject(object);
            byte[] bytes = out.toByteArray();
            jsonString = new String(bytes, Charset.forName("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonString;
    }

    public static byte[] toJsonByteArray(Object object) {
        return toJsonString(object).getBytes(Charset.forName("utf-8"));
    }
}
