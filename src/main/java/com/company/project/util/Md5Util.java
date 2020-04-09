package com.company.project.util;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.util.Random;

/**
 * MD5加密工具类
 *
 */
@Slf4j
public class Md5Util {

    /**
     * 通过盐值对字符串进行MD5加密
     *
     * @param param 需要加密的字符串
     * @param salt  盐值
     * @return
     */
    public static String MD5(String param, String salt) {
        return MD5(param + salt);
    }

    /**
     * 加密字符串
     *
     * @param s 字符串
     * @return
     */
    public static String MD5(String s) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            log.error("MD5生成失败", e);
            return null;
        }
    }

    /**
     * description 获取指定位数的随机数
     *
     * @param length 1
     * @return java.lang.String
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
