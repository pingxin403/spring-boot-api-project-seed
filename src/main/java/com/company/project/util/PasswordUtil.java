package com.company.project.util;

import com.company.project.business.consts.ProjectConstant;
import com.sun.deploy.association.utility.AppConstants;

/**
 * @author hyp
 * Project name is blog
 * Include in com.hyp.learn.core.utils
 * hyp create at 20-3-18
 **/
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



    public static boolean matches(String salt, String oldPwd, String password) throws Exception {
        return password.equals(encrypt(oldPwd,salt));
    }

    public static void main(String[] args) throws Exception {
        String s = Md5Util.MD5("admin"+ ProjectConstant.APP_SECURITY_KEY);
        String encrypt = encrypt("666666", s);
        System.out.println(s);
        System.out.println(encrypt);
        System.out.println(decrypt(encrypt,s));
    }
}
