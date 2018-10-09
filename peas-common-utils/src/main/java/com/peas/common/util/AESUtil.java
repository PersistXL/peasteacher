package com.peas.common.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * AES加解密 Created by duanyihui on 2016/6/27.
 */
public class AESUtil
{

    private static final String KEY = "AES";

    private static final int KEY_LEN = 128;

    /**
     * 加密
     *
     * @param content 待加密内容
     * @param password 密钥
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(String content, String password) throws Exception
    {
        KeyGenerator kgen = KeyGenerator.getInstance(KEY);
        kgen.init(KEY_LEN, new SecureRandom(password.getBytes("utf-8")));
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, KEY);
        Cipher cipher = Cipher.getInstance(KEY);// 创建密码器
        byte[] byteContent = content.getBytes("utf-8");
        cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
        byte[] result = cipher.doFinal(byteContent);
        return result; // 加密
    }

    public static String encryptWithBase64(String content, String password) throws Exception
    {
        byte[] bytes = encrypt(content, new String(RSAUtil.decryptBASE64(password), "utf-8"));
        return RSAUtil.encryptBASE64(bytes);
    }

    /**
     * 解密
     *
     * @param content 待解密内容
     * @param password 密钥
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] content, String password) throws Exception
    {
        KeyGenerator kgen = KeyGenerator.getInstance(KEY);
        kgen.init(KEY_LEN, new SecureRandom(password.getBytes("utf-8")));
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, KEY);
        Cipher cipher = Cipher.getInstance(KEY);// 创建密码器
        cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
        byte[] result = cipher.doFinal(content);
        return result; // 加密
    }

    public static String decrypt(String base64Str, String password, String iv) throws Exception
    {
        SecretKeySpec secretKeySpec = new SecretKeySpec(password.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv.getBytes()));
        // 该Base64类（org.apache.commons.codec.binary.Base64）运行正常
        // 如果用Base64Util，可能会报告Illegal base64 character a
        byte[] result = cipher.doFinal(Base64.decodeBase64(base64Str));
        return new String(result, "UTF-8");

    }

    public static String encrypt(String content, String password, String iv) throws Exception
    {
        SecretKeySpec secretKeySpec = new SecretKeySpec(password.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(iv.getBytes()));
        byte[] result = cipher.doFinal(content.getBytes());
        return Base64Util.encrypt(result);

    }

    public static String decryptWithBase64(String base64Content, String password) throws Exception
    {
        byte[] bytes = decrypt(RSAUtil.decryptBASE64(base64Content),
                new String(RSAUtil.decryptBASE64(password), "utf-8"));
        return new String(bytes, "utf-8");
    }
}
