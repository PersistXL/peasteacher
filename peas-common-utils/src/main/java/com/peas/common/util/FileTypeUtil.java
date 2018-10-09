package com.peas.common.util;

import org.apache.commons.codec.binary.Hex;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 文件类型工具 Created by duan on 2016/1/7.
 */
public class FileTypeUtil {

    private FileTypeUtil() {
    }

    public static String typeOf(String file) {
        return typeOf(new File(file));
    }

    public static String typeOf(File file) {
        byte[] head = read128(file);
        return typeOf(head);
    }

    public static String typeOf(byte[] head) {
        String header = new String(Hex.encodeHex(head, false));
        if (DicomUtil.isDicomFile(head)) {
            return "DCM";
        } else if (header.startsWith("FFD8FF")) {
            return "JPG";
        } else if (header.startsWith("89504E47")) {
            return "PNG";
        } else if (header.startsWith("47494638")) {
            return "GIF";
        } else if (header.startsWith("424D")) {
            return "BMP";
        } else if (header.startsWith("504B0708") || header.startsWith("504B0304")) {
            return "ZIP";
        } else if (header.startsWith("52617221")) {
            return "RAR";
        }
        return "UN";
    }

    private static byte[] read128(File path) {
        byte[] buf128 = new byte[128];
        for (int i = 0; i < buf128.length; i++) {
            buf128[i] = -1;
        }
        try (FileInputStream in = new FileInputStream(path)) {
            int i = in.read(buf128);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buf128;
    }

    private static byte[] read128(byte[] file) {
        byte[] buf128 = new byte[128];
        for (int i = 0; i < buf128.length; i++) {
            if (i < file.length) {
                buf128[i] = file[i];

            } else {
                buf128[i] = -1;
            }
        }
        return buf128;
    }
}
