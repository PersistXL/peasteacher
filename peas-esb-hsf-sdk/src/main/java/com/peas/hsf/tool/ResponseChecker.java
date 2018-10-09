package com.peas.hsf.tool;

import com.google.common.base.Preconditions;
import com.peas.hsf.http.ErrorCode;

import java.util.Map;

/**
 * @Author duanyihui
 * @Date 2016/4/5
 * @Description
 */
public class ResponseChecker {

    private ResponseChecker() {
    }

    /**
     * @param json
     * @return
     */
    public static boolean isOk(Object json) {
        if (json instanceof String) {
            String s = (String) json;
            if (!GsonUtil.isJsonObject(s)) {
                return true;
            }
            if (s.startsWith("[")) {
                return true;
            }
            if (s.startsWith("{")) {
                Map<String, Object> map = GsonUtil.parseToMap(s);
                if (map.containsKey("$type")) {
                    Preconditions.checkState(!Integer.valueOf(ErrorCode.ERROR).equals(map.get("code")), String.format("ERROR-MESSAGE : \t %s", s));
                }
                return true;
            }
        } else if (json instanceof byte[]) {
            return true;
        }
        return true;
    }
}
