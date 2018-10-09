package com.peas.hsf.tool;

import lombok.extern.log4j.Log4j;

import java.io.*;

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
     */
    public static byte[] read(InputStream in, boolean closeAuto) {
        byte[] result = null;
        int i = 0;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            while ((i = in.read()) != -1) {
                baos.write(i);
                baos.flush();
            }
            result = baos.toByteArray();
        } catch (Exception e) {
            log.debug("read stream error ", e);
        } finally {
            if (closeAuto && in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.debug("read stream close error ", e);
                }
            }
        }
        return result;
    }


    public static byte[] asBytes(Serializable serializable) {
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        byte[] data = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(serializable);
            oos.flush();
            data = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(oos, bos);
        }
        return data;
    }

    public static Object readObject(byte[] bytes) {
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        Object o = null;
        try {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            o = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(ois, bis);
        }
        return o;
    }

    public static <T> T readObject(byte[] bytes, Class<T> t) {
        return (T) readObject(bytes);
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


    public static void close(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        byte[] bytes = StreamUtil.asBytes(new int[]{1, 2, 3});
        int[] o = StreamUtil.readObject(bytes, int[].class);
        System.out.println(o.length);
    }
}
