package com.company.project.framework.shiro.filter;

import com.company.project.business.consts.JwtConstant;
import com.company.project.business.service.RedisService;
import com.company.project.framework.exception.BusinessException;
import com.company.project.framework.exception.code.BaseResponseCode;
import com.company.project.util.RequestUtil;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 验证码拦截器继承了AccessControlFilter，该类提供了访问控制的基础功能，比如是否允许访问/当访问拒绝时如何处理等。主要有两个方法：
 * <p>
 * isAccessAllowed：表示是否允许访问；mappedValue就是[urls]配置中拦截器参数部分，如果允许访问返回true，否则false；
 * onAccessDenied：表示当访问拒绝时是否已经处理了；如果返回true表示需要继续处理；如果返回false表示该拦截器实例已经处理了，将直接返回即可
 *
 * @author hyp
 * Project name is spring-boot-learn
 * Include in com.hyp.learn.shiro.shiro.filter
 * hyp create at 20-3-30
 **/
public class CaptchaValidateFilter extends AccessControlFilter {
    private String captchaParam = JwtConstant.KAPTCHA_CACHE_KEY; //前台提交的验证码参数名

    @Autowired
    private RedisService redisService;

    public String getCaptchaCode(ServletRequest request) {
        return RequestUtil.getHeader(getCaptchaParam());
    }

    private String getCaptchaParam() {
        return captchaParam;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse servletResponse, Object o) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        // 从session获取正确的验证码

        String uuid = RequestUtil.getHeader(JwtConstant.CAPTCHA_PARAM);
        String validateCode = (String) redisService.get(JwtConstant.KAPTCHA_CACHE_KEY + uuid);

        //页面输入的验证码
        String captchaCode = getCaptchaCode(request);


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
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse servletResponse) throws Exception {
        //如果验证码失败了，存储失败key属性
        throw new BusinessException(BaseResponseCode.INVALID_AUTHCODE);
    }
}
