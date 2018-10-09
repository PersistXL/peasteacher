package com.peas.common.base;

import com.peas.common.util.MD5Util;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by peaimage on 2016/8/12.
 */
public class FileMonitor {

    public static void monitor(String file, long minutes, Handle handle) {
        String[] md5 = new String[]{md5(file)};
        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(() -> {
            String newMd5 = md5(file);
            if (!newMd5.equals(md5[0])) {
                md5[0] = newMd5;
                handle.handle(file);
            }
        }, minutes, minutes, TimeUnit.MINUTES);
    }

    private static String md5(String path) {
        try {
            byte[] bytes = Files.readAllBytes(java.nio.file.Paths.get(path));
            return MD5Util.sign(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static interface Handle {
        void handle(String file);
    }
}
