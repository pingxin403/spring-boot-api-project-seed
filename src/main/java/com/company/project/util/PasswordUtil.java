package com.company.project.util;


import com.company.project.business.consts.ProjectConstant;

import java.util.UUID;

public class PasswordUtil {

    /**
     * AES 加密
     *
     * @param password 未加密的密码
     * @param salt     盐值，默认使用用户名就可
     * @return
     * @throws Exception
     */
    public static String encrypt(String password, String salt) throws Exception {
        return AesUtil.encrypt(Md5Util.MD5(salt + ProjectConstant.APP_SECURITY_KEY), password);
    }

    /**
     * AES 解密
     *
     * @param encryptPassword 加密后的密码
     * @param salt            盐值，默认使用用户名就可
     * @return
     * @throws Exception
     */
    public static String decrypt(String encryptPassword, String salt) throws Exception {
        return AesUtil.decrypt(Md5Util.MD5(salt + ProjectConstant.APP_SECURITY_KEY), encryptPassword);
    }

    /**
     * 匹配密码
     *
     * @param salt    盐
     * @param rawPass 明文
     * @param encPass 密文
     * @return
     */
    public static boolean matches(String salt, String rawPass, String encPass) {
        return new PasswordEncoder(salt).matches(encPass, rawPass);
    }

    /**
     * 明文密码加密
     *
     * @param rawPass 明文
     * @param salt
     * @return
     */
    public static String encode(String rawPass, String salt) {
        return new PasswordEncoder(salt).encode(rawPass);
    }

    /**
     * 获取加密盐
     *
     * @return
     */
    public static String getSalt() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
    }

    public static void main(String[] args) {
        System.out.println(PasswordUtil.encode("666666", "324ce32d86224b00a02b"));
    }
}
