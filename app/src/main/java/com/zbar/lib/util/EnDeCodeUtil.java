package com.zbar.lib.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Created by Administrator on 2017/2/22.
 */

public class EnDeCodeUtil {
    //UUID 是指在一台机器上生成的数字，它保证对在同一时空中的所有机器都是唯一的。
    //结果：4e608182-704b-4411-8cb7-e3312e144caa
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
    /**
     * 利用 UUID 生成一个32位的随机数。防止重复，保留唯一性
     * 6c0222ed-e7f5-4cad-a717-a9abfb372239（36位）
     * 6c0222ede7f54cada717a9abfb372239（32位）
     */
    public static String getUUID32(String[] args) {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }
    //字节数组转化为16进制字符串
    public static String byteArrayToHex(byte[] byteArray) {
        char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };
        char[] resultCharArray =new char[byteArray.length * 2];
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        return new String(resultCharArray);
    }

    //字节数组md5算法
    public static byte[] MD5 (byte [] bytes) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(bytes);
            return messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    //字符串md5算法
    public static String MD5(String s) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

}
