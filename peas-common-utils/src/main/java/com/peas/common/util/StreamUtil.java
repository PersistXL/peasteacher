package com.peas.common.util;

import com.google.common.io.ByteStreams;
import com.peas.common.base.Objects;
import lombok.extern.log4j.Log4j;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 读取流中的内容
 *
 * @author dyh 2015年2月3日
 * @see
 * @since 1.0
 */
@Log4j
public class StreamUtil {

    /**
     * 私有构造器
     */
    private StreamUtil() {
    }

    /**
     * 读取流中数据
     *
     * @param in        输入流
     * @param closeAuto 是否自动关闭流
     * @return
     * @throws IOException
     */
    public static byte[] read(InputStream in, boolean closeAuto) {
        byte[] bytes = null;
        try {
            bytes = ByteStreams.toByteArray(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (closeAuto && !Objects.isNullOrEmpty(in)) {
                close(in);
            }
        }
        return bytes;
    }

    /**
     * 关闭流
     *
     * @param cs
     */
    public static void close(Closeable... cs) {
        for (Closeable c : cs) {
            if (c != null) {
                try {
                    c.close();
                } catch (IOException e) {
                    log.error("close error", e);
                }
            }
        }
    }

    /**
     * 读取流中数据 并且自动关闭
     *
     * @param in 输入流
     * @return
     */
    public static byte[] read(InputStream in) {
        return read(in, true);
    }

    /**
     * 从输入流复制到输出流
     *
     * @param from
     * @param to
     */
    public static void copy(InputStream from, OutputStream to) {
        try {
            ByteStreams.copy(from, to);
        } catch (IOException e) {
            log.error("", e);
        } finally {
            close(from, to);
        }
    }
}
