package com.peas.common.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * UUID生成器
 *
 * @author dyh
 */
public class UUIDUtil {

    /**
     * 纯数字
     */
    public static final int NUM = 1;

    /**
     * 小写字母
     */
    public static final int LOWER = 2;

    /**
     * 大写字母
     */
    public static final int UPPER = 4;

    private static final int MAX_UUID_NUM = 3;

    private static final Map<Integer, char[]> maps = Maps.newConcurrentMap();

    private static final Random RANDOM = new Random(System.nanoTime());

    /**
     * 随机短UUID (PS:4-24位)
     *
     * @return
     */
    public static String randomShortUUID(int length, int type) {
        Preconditions.checkArgument(length >= 4 && length <= 24, "length must be between 4 and 24");
        Preconditions
                .checkArgument(NUM == type ? length >= 4 && length <= 24 : true, "num code must be between 4 and 24");
        StringBuffer sb = new StringBuffer();
        String uuid = random3UUIDs();
        char[] cs = createChars(type);
        for (int i = 0; i < length; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16) + RANDOM.nextInt(0xFFFF);
            sb.append(cs[x % cs.length]);
        }
        return sb.toString();
    }

    /**
     * 随机UUID 标准格式(含“-”)
     *
     * @return
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 随机UUID 32位长不含“-”
     *
     * @return
     */
    public static String randomUUIDw() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private static String random3UUIDs() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < MAX_UUID_NUM; i++) {
            sb.append(UUID.randomUUID().toString());
        }
        return sb.toString().replace("-", "");
    }

    private static char[] createChars(int type) {
        if (maps.containsKey(type)) {
            return maps.get(type);
        }
        StringBuilder sb = new StringBuilder();
        if (NUM == (NUM & type)) {
            sb.append(create('0', 10));
        }
        if (LOWER == (LOWER & type)) {
            sb.append(create('a', 26));
        }
        if (UPPER == (UPPER & type)) {
            sb.append(create('A', 26));
        }
        char[] cs = sb.toString().toCharArray();
        maps.put(type, cs);
        return cs;
    }

    private static String create(char start, int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append((char) (start + i));
        }
        return sb.toString();
    }

}
