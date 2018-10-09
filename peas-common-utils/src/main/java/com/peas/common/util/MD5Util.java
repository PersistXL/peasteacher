package com.peas.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

import lombok.extern.log4j.Log4j;
import sun.nio.ch.FileChannelImpl;

/**
 * MD5 加密工具
 *
 * @author zhc 2015-4-13
 */
@Log4j
public class MD5Util
{

    /**
     * php-MD5加密
     *
     * @param sign
     * @return
     */
    public static String sign2PhpByMd5(String sign)
    {
        MessageDigest md = null;
        try
        {
            md = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        log.debug("加密之前的字符串[" + sign + "]");
        try
        {
            md.update(sign.getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        } // MD5加密算法只是对字符数组而不是字符串进行加密计算，得到要加密的对象
        byte[] bs = md.digest(); // 进行加密运算并返回字符数组
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bs.length; i++)
        { // 字节数组转换成十六进制字符串，形成最终的密文
            /*
             * int v = bs[i] & 0xff; if (v < 16) { sb.append(0); } else {
             * sb.append(Integer.toHexString(v)); }
             */
            sb.append(String.format("%02x", bs[i] & 0xff));
        }
        return sb.toString();
    }

    /**
     * MD5加密
     *
     * @param sign
     * @return
     */
    public static String signByMd5(String sign)
    {
        MessageDigest md = null;
        try
        {
            md = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        log.debug("加密之前的字符串[" + sign + "]");
        try
        {
            md.update(sign.getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        } // MD5加密算法只是对字符数组而不是字符串进行加密计算，得到要加密的对象
        byte[] bs = md.digest(); // 进行加密运算并返回字符数组
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bs.length; i++)
        { // 字节数组转换成十六进制字符串，形成最终的密文
            int v = bs[i] & 0xff;
            if (v < 16)
            {
                sb.append(0);
            }
            else
            {
                sb.append(Integer.toHexString(v));
            }
        }
        return sb.toString();
    }

    /**
     * 计算文件的md5码
     *
     * @param file 单个文件
     * @return 计算结果
     * @description 整个文件的md5码
     * @description 整个文件的md5码
     * @Author He Enqi
     * @Date 2015年5月28日
     */
    public static String getMd5ByFile(File file)
    {
        String value = "";

        try (FileInputStream in = new FileInputStream(file);)
        {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            value = new String(Hex.encodeHex(md5.digest()));
            Method m = FileChannelImpl.class.getDeclaredMethod("unmap", MappedByteBuffer.class);
            m.setAccessible(true);
            m.invoke(FileChannelImpl.class, byteBuffer);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return value;
    }

    public static String sign(byte[] data)
    {
        byte[] result = new byte[0];
        try
        {
            result = RSAUtil.encryptMD5(data);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        String value = new String(Hex.encodeHex(result));
        return value;
    }

    public static String strToMD5(String sourceStr)
    {
        String result = "";
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++)
            {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
