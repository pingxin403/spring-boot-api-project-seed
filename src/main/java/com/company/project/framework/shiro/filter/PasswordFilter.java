package com.company.project.framework.shiro.filter;


import com.company.project.business.consts.JwtConstant;
import com.company.project.business.service.RedisService;
import com.company.project.framework.exception.BusinessException;
import com.company.project.framework.exception.code.BaseResponseCode;
import com.company.project.framework.shiro.token.JwtToken;
import com.company.project.framework.shiro.token.PasswordToken;
import com.company.project.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class PasswordFilter extends AccessControlFilter {

    private RedisService redisService;

    private boolean isEncryptPassword;

    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    public void setEncryptPassword(boolean encryptPassword) {
        isEncryptPassword = encryptPassword;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object o) {
        Subject subject = getSubject(request, response);
        // 如果其已经登录，再此发送登录请求
        //  拒绝，统一交给 onAccessDenied 处理
        return null != subject && subject.isAuthenticated();
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (isPasswordTokenGet(request)) {
            //动态生成秘钥，redis存储秘钥供之后秘钥验证使用，设置有效期5秒用完即丢弃
            String tokenKey = Md5Util.getRandomString(16);
            String userKey = Md5Util.getRandomString(6);
            try {
                redisService.set(JwtConstant.TOKEN_KEY + IpUtil.getRealIp(WebUtils.toHttp(request)).toUpperCase() + userKey.toUpperCase(), tokenKey, 5, TimeUnit.SECONDS);
                // 动态秘钥response返回给前端

                Map<String, String> data = new HashMap<>();
                data.put("tokenKey", tokenKey);
                data.put("userKey", userKey.toUpperCase());
                RequestUtil.responseWrite(ResultUtil.success(data).toJson(), servletResponse);

            } catch (Exception e) {
                log.warn("签发动态秘钥失败" + e.getMessage(), e);
                throw new BusinessException(BaseResponseCode.DATA_ERROR);
            }
            return false;
        }

        // 判断是否是登录请求
        if (isPasswordLoginPost(request)) {

            AuthenticationToken authenticationToken;
            try {
                authenticationToken = createPasswordToken(request);
            } catch (Exception e) {
                // response 告知无效请求
                throw new BusinessException(BaseResponseCode.INVALID_ACCESS);
            }
            try {
                Subject subject = getSubject(servletRequest, servletResponse);
                System.out.println(subject.isAuthenticated() + "");

                System.out.println(HttpContextUtils.isAjaxRequest(request));
                log.info(request.getMethod());
                log.info(request.getRequestURL().toString());
                String token = request.getHeader(JwtConstant.ACCESS_TOKEN);
                if (StringUtils.isEmpty(token)) {
                    throw new BusinessException(BaseResponseCode.TOKEN_ERROR);
                }
                JwtToken jwtToken = JwtToken.createJwtToken(servletRequest);
                getSubject(servletRequest, servletResponse).login(jwtToken);
                return true;
            } catch (BusinessException exception) {
                if (exception.getCode() == BaseResponseCode.TOKEN_ERROR.getCode()) {
                    throw new BusinessException(BaseResponseCode.TOKEN_ERROR);
                } else if (exception.getCode() == BaseResponseCode.UNAUTHORIZED_ERROR.getCode()) {
                    throw new BusinessException(BaseResponseCode.UNAUTHORIZED_ERROR);
                } else {
                    throw new BusinessException(BaseResponseCode.ERROR);
                }
            } catch (Exception e) {
                throw new BusinessException(BaseResponseCode.SYSTEM_BUSY);
            }

        }

        // 判断是否为注册请求,若是通过过滤链进入controller注册
        if (isAccountRegisterPost(request)) {
            return true;
        }

        return false;
    }

    private boolean isPasswordTokenGet(ServletRequest request) {

        String tokenKey = RequestUtil.getHeader(JwtConstant.ACCESS_TOKEN);

        return (request instanceof HttpServletRequest)
                && "GET".equals(((HttpServletRequest) request).getMethod().toUpperCase())
                && "get".equals(tokenKey);
    }

    private boolean isPasswordLoginPost(ServletRequest request) {

        Map<String, String> map = RequestUtil.getRequestBodyMap(request);
        String password = map.get("password");
        String timestamp = map.get("timestamp");
        String methodName = map.get("methodName");
        String appId = map.get(JwtConstant.APP_ID);
        return (request instanceof HttpServletRequest)
                && "POST".equals(((HttpServletRequest) request).getMethod().toUpperCase())
                && null != password
                && null != timestamp
                && null != appId
                && "login".equals(methodName);
    }

    private boolean isAccountRegisterPost(ServletRequest request) {

        Map<String, String> map = RequestUtil.getRequestBodyMap(request);
        String uid = map.get("uid");
        String username = map.get("username");
        String methodName = map.get("methodName");
        String password = map.get("password");
        return (request instanceof HttpServletRequest)
                && "POST".equals(((HttpServletRequest) request).getMethod().toUpperCase())
                && null != username
                && null != password
                && null != uid
                && "register".equals(methodName);
    }

    private AuthenticationToken createPasswordToken(ServletRequest request) throws Exception {

        Map<String, String> map = RequestUtil.getRequestBodyMap(request);
        String appId = map.get("appId");
        String timestamp = map.get("timestamp");
        String password = map.get("password");
        String host = IpUtil.getRealIp(WebUtils.toHttp(request));
        String userKey = map.get("userKey");
        if (isEncryptPassword) {
            String tokenKey = redisService.get(JwtConstant.TOKEN_KEY + host.toUpperCase() + userKey);
            password = AesUtil.decrypt(password, tokenKey);
        }
        return new PasswordToken(appId, password, timestamp, host);
    }

}
