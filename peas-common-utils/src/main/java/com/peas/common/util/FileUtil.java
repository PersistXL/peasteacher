package com.peas.common.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import lombok.extern.log4j.Log4j;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

/**
 * 文件操作
 *
 * @author dyh
 */
@Log4j
public class FileUtil {

    public static final String READ_ONLY = "r";

    private FileUtil() {
    }

    /**
     * @param filePath 文件路径
     * @param cs       编码
     * @return
     * @throws IOException
     */
    public static byte[] readAllBytes(String filePath, Charset cs) throws IOException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(filePath), "file path can not be null");
        return Files.toByteArray(new File(filePath));
    }

    /**
     * @param filePath 文件路径
     * @return
     * @throws IOException
     */
    public static byte[] readAllBytes(String filePath) throws IOException {
        return readAllBytes(filePath, Charset.forName("utf-8"));
    }

    /**
     * 读取所有行
     *
     * @param filePath 文件路徑
     * @param charset  編碼格式
     * @return
     * @throws IOException
     */
    public static List<String> readAllLines(String filePath, Charset charset) throws IOException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(filePath), "file path can not be null");
        return Files.readLines(new File(filePath), charset);
    }

    /**
     * 读取所有行
     *
     * @param filePath 文件路徑
     * @return
     * @throws IOException
     */
    public static List<String> readAllLines(String filePath) throws IOException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(filePath), "file path can not be null");
        return Files.readLines(new File(filePath), Charset.forName("utf-8"));
    }

    /**
     * 向文件中追加内容
     *
     * @param content 新内容
     * @param tofile  文件
     * @param cs      編碼
     * @throws IOException
     */
    public static void appenTo(String content, File tofile, Charset cs) throws IOException {
        Files.append(content, tofile, cs);
    }

    /**
     * 向文件中追加内容
     *
     * @param content 新内容
     * @param tofile  文件
     * @throws IOException
     */
    public static void appenTo(String content, File tofile) throws IOException {
        Files.append(content, tofile, Charset.forName("utf-8"));
    }

    /**
     * 读取变化的
     *
     * @param file 文件
     * @return
     * @throws FileNotFoundException
     */
    public static String readChangeContent(RandomAccessFile file, Charset cs) throws FileNotFoundException {
        byte[] r = new byte[0];
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int t = 0;
            while ((t = file.read()) != -1) {
                out.write(t);
                out.flush();
            }
            r = out.toByteArray();
        } catch (Exception e) {
            log.error("", e);
        }
        return new String(r, cs);
    }

    public static void copy(File from, File to) {
        if (from.isFile()) {
            fileCopy(from, to);
        } else {
            File[] list = from.listFiles();
            String source = from.getAbsolutePath();
            String destination = to.getAbsolutePath();
            for (File file : list) {
                String relativePath = file.getAbsolutePath().substring(source.length());
                String newPath = destination + relativePath;
                if (file.isFile()) {
                    fileCopy(file, new File(newPath));
                } else {
                    File f = new File(newPath);
                    if (!f.exists()) {
                        boolean b = f.mkdirs();
                        log.debug("create :" + f.getAbsolutePath() + "     " + b);
                    }
                    copy(file, f);
                }
            }
        }
    }

    private static void fileCopy(File from, File to) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(from);
            out = new FileOutputStream(to);
            StreamUtil.copy(in, out);
        } catch (Exception e) {
            log.error("", e);
        } finally {
            StreamUtil.close(out, in);
        }
    }

    /**
     * 删除目录
     *
     * @param dir
     * @return
     */
    public static boolean deleteDirectory(File dir) {
        Preconditions.checkArgument(dir != null, "dir can not be null");
        Preconditions.checkArgument(dir.isDirectory(), String.format("[%s] must be dir", dir.getAbsolutePath()));
        delete(dir);
        return !dir.exists();
    }

    @SuppressWarnings({"unused"})
    public static boolean write(byte[] bytes, File toFile) {
        try {
            try (ByteArrayInputStream in = new ByteArrayInputStream(bytes)) {
                byte[] buffer = new byte[1024];
                try (FileOutputStream out = new FileOutputStream(toFile)) {
                    int bytesum = 0;
                    int byteread = 0;
                    while ((byteread = in.read(buffer)) != -1) {
                        bytesum += byteread;
                        out.write(buffer, 0, byteread); // 文件写操作
                    }
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
        return true;
    }

    private static void delete(File dir) {
        File[] cs = dir.listFiles();
        for (File c : cs) {
            if (c.isDirectory()) {
                delete(c);
            } else {
                if (!c.delete()) {
                    log.debug(String.format("delete File:[%s] fail,path:%[%s]", c.getName(), c.getAbsolutePath()));
                }
            }
        }
        if (!dir.delete()) {
            log.debug(String.format("delete directory:[%s] fail,path:%[%s]", dir.getName(), dir.getAbsolutePath()));
        }
    }

    public static void addSuffix(File file, String suffix) {
        Preconditions.checkArgument(file.isFile(), file.getAbsoluteFile() + " is not file");
        StringBuilder sb = new StringBuilder(file.getAbsolutePath());
        sb.append(".");
        sb.append(suffix);
        file.renameTo(new File(sb.toString()));
    }

    public static String removeSuffix(String filePath) {
        int index = filePath.lastIndexOf(".");
        return filePath.substring(0, index);
    }

    public static String getSuffix(String filePath) {
        int index = filePath.lastIndexOf(".");
        return filePath.substring(index);
    }

    private static String getTempFilePathFormat(File file) {
        String parentPath = file.getParent();
        String fileName = removeSuffix(file.getName());
        return parentPath + "/" + file.getName() + "_" + System.currentTimeMillis() + "/" + fileName + "_%s" + getSuffix(file.getName());
    }

    public static String split(File file, int size) {
        return split(file, null, size);
    }


    public static String split(File file, String dest, int size) {
        int length = size * 1024;
        long fileLength = file.length();
        int lastOne = (int) (fileLength % length);
        int num = (int) (lastOne == 0 ? fileLength / length : (fileLength / length + 1));
        String tmp = null;
        if (Strings.isNullOrEmpty(dest)) {
            tmp = getTempFilePathFormat(file);
        } else {
            tmp = dest;
        }
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            for (int i = 0; i < num; i++) {
                byte[] buf;
                if (lastOne != 0 && i + 1 < num) {
                    buf = new byte[length];
                } else {
                    buf = new byte[lastOne];
                }
                fileInputStream.read(buf);
                File to = new File(String.format(tmp, i + 1));
                if (!to.getParentFile().exists()) {
                    to.getParentFile().mkdirs();
                }
                Files.write(buf, to);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File(tmp).getParentFile().getAbsolutePath();
    }

    public static void join(File dir, String newPath) {
        String basePath = dir.getAbsolutePath();
        int index = dir.getName().lastIndexOf("_");
        String fileName = dir.getName().substring(0, index);
        String newFile = newPath + "/" + fileName;
        List<String> list = Lists.newArrayList(dir.list());
        list.sort(Comparator.comparingInt(FileUtil::getIndex));
        byte[] fullData = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            for (int i = 0, size = list.size(); i < size; i++) {
                byte[] data = java.nio.file.Files.readAllBytes(Paths.get(basePath + "/" + list.get(i)));
                bos.write(data);
            }
            bos.flush();
            fullData = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File nf = new File(newFile);
        try {
            if (!nf.getParentFile().exists()) {
                nf.getParentFile().mkdirs();
            }
            Files.write(fullData, nf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getIndex(String name) {
        name = name.substring(name.lastIndexOf("_"));
        int index = name.lastIndexOf(".");
        name = index == -1 ? name.replace("_", "") : name.substring(1, index);
        return Integer.valueOf(name);
    }
}
