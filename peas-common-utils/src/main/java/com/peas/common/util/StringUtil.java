package com.peas.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by duan on 2016/2/23.
 */
public class StringUtil {

    private StringUtil() {
    }

    /**
     * 小写首字母
     *
     * @param word
     * @return
     */
    public final static String firstCharacterLowerCase(String word) {
        String first = word.substring(0, 1);
        String end = word.substring(1);
        return first.toLowerCase() + end;
    }

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否是乱码
     *
     * @param strName 字符串
     * @return 是否是乱码
     */
    public static boolean isMessyCode(String strName) {
        Pattern p = Pattern.compile("\\s*|t*|r*|n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = ch.length;
        float count = 0;
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c)) {
                if (!isChinese(c)) {
                    count = count + 1;
                }
            }
        }
        float result = count / chLength;
        if (result > 0.4) {
            return true;
        } else {
            return false;
        }
    }
    
    public static String trimOn(String str, String on) {
		str = str.trim();
		if (!str.contains(on)) {
			return str;
		}
		for (;;) {
			int len1 = str.indexOf(on);
			if (len1 == 0) {
				str = str.substring(on.length());
			}
			if (str.equals("")) {
				return "";
			}
			int len2 = str.lastIndexOf(on);
			if (len2 == str.length() - on.length() && len2 >= 0) {
				str = str.substring(0, len2);
			}
			if (str.indexOf(on) == -1 || str.indexOf(on) != 0) {
				if (str.lastIndexOf(on) == -1 || str.lastIndexOf(on) < str.length() - on.length()) {
					break;
				}
			}
		}
		return str;
	}
}
