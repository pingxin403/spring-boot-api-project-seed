package com.company.project.util;

import com.company.project.business.consts.JwtConstant;
import com.company.project.business.service.RedisService;
import com.company.project.framework.holder.RequestHolder;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hyp
 * Project name is spring-boot-api-project-seed
 * Include in com.company.project.util
 * hyp create at 20-4-11
 **/
public class CaptchaUtil {

    public static boolean matchs(RedisService redisService,String captchaCode) {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(RequestHolder.getRequest());
        // 从header获取正确的验证码

        String uuid = RequestUtil.getHeader(JwtConstant.CAPTCHA_PARAM);
        String validateCode = (String) redisService.get(JwtConstant.KAPTCHA_CACHE_KEY + uuid);


        //判断验证码是否表单提交（允许访问）
        if (!"post".equalsIgnoreCase(httpServletRequest.getMethod())) {
            return true;
        }


        // 若验证码为空或匹配失败则返回false
        if (captchaCode == null) {
            return false;
        } else if (validateCode != null) {
            captchaCode = captchaCode.toLowerCase();
            validateCode = validateCode.toLowerCase();
            if (!captchaCode.equals(validateCode)) {
                return false;
            }
        }
        redisService.del(JwtConstant.KAPTCHA_CACHE_KEY + uuid);
        return true;
    }
}
