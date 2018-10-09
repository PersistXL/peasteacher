package com.peas.common.util;

import com.google.common.io.BaseEncoding;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.SecureRandom;

/**
 * DES加密工具
 *
 * @Description
 * @Author He Enqi
 * @Date 2015年6月8日 下午1:46:51
 */
public class DESUtil {
    Key key;

    /**
     * 构造方法
     *
     * @param str 加密KEY
     * @throws Exception
     */
    private DESUtil(String str) throws Exception {
        // 生成秘钥
        setKey(str);
    }

    /**
     * 构造方法
     *
     * @throws Exception
     */
    private DESUtil() throws Exception {
        setKey("PEADRAUDI");
    }

    public static DESUtil getInstance() throws Exception {
        return InnerClass.getInstance("");
    }

    public static DESUtil getInstance(String key) throws Exception {
        return InnerClass.getInstance(key);
    }

    /**
     * 根据参数生成KEY
     *
     * @param strKey KEY
     * @throws Exception 异常
     * @description
     * @Author He Enqi
     * @Date 2015年6月8日
     */
    public void setKey(String strKey) throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("DES");
        generator.init(new SecureRandom(strKey.getBytes()));
        this.key = generator.generateKey();
        generator = null;
    }

    /**
     * 加密String明文输入,String密文输出
     *
     * @param strMing 需要加密的字符串
     * @return 密文
     * @throws Exception 异常
     * @description
     * @Author He Enqi
     * @Date 2015年6月8日
     */
    public String getEncString(String strMing) throws Exception {
        byte[] byteMing = null;
        byte[] byteMi = null;
        String strMi = "";
        byteMing = strMing.getBytes("UTF8");
        byteMi = this.getEncCode(byteMing);
        strMi = BaseEncoding.base64().encode(byteMi);
        return strMi;
    }

    /**
     * 解密 以String密文输入,String明文输出
     *
     * @param strMi 密文
     * @return 解密后的字符串
     * @throws Exception 异常
     * @description
     * @Author He Enqi
     * @Date 2015年6月8日
     */
    public String getDesString(String strMi) throws Exception {
        byte[] byteMi = BaseEncoding.base64().decode(strMi);
        byte[] byteMing = null;
        String strMing = "";
        byteMing = this.getDesCode(byteMi);
        strMing = new String(byteMing, "UTF8");
        return strMing;
    }

    /**
     * 加密以byte[]明文输入,byte[]密文输出
     *
     * @param byteS
     * @return
     * @throws Exception
     * @description
     * @Author He Enqi
     * @Date 2015年6月8日
     */
    private byte[] getEncCode(byte[] byteS) throws Exception {
        byte[] byteFina = null;
        Cipher cipher;
        cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byteFina = cipher.doFinal(byteS);
        return byteFina;
    }

    /**
     * 解密以byte[]密文输入,以byte[]明文输出
     *
     * @param byteD
     * @return
     * @throws Exception
     * @description
     * @Author He Enqi
     * @Date 2015年6月8日
     */
    private byte[] getDesCode(byte[] byteD) throws Exception {
        Cipher cipher;
        byte[] byteFina = null;
        cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byteFina = cipher.doFinal(byteD);
        return byteFina;
    }

    private static class InnerClass {
        private static DESUtil getInstance(String key) throws Exception {
            if (key.isEmpty()) {
                return new DESUtil();
            } else {
                return new DESUtil(key);
            }

        }
    }
}
