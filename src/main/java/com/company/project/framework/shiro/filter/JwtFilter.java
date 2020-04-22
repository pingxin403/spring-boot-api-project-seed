package com.company.project.framework.shiro.filter;

import com.company.project.business.consts.JwtConstant;
import com.company.project.framework.exception.BusinessException;
import com.company.project.framework.exception.code.BaseResponseCode;
import com.company.project.framework.shiro.token.JwtToken;
import com.company.project.util.HttpContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 支持restful url 的过滤链  JWT json web token 过滤器，无状态验证
 *
 * @author hyp
 * Project name is spring-boot-api-project-seed
 * Include in com.company.project.framework.shiro.filter
 * hyp create at 20-4-8
 **/
@Slf4j
public class JwtFilter extends AccessControlFilter {


    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        try {
            Subject subject = getSubject(servletRequest, servletResponse);
            System.out.println(subject.isAuthenticated() + "");

            System.out.println(HttpContextUtils.isAjaxRequest(request));

            String token = request.getHeader(JwtConstant.ACCESS_TOKEN);
            if (StringUtils.isEmpty(token)) {
                throw new BusinessException(BaseResponseCode.TOKEN_ERROR);
            }

            JwtToken jwtPasswordToken = JwtToken.createJwtToken(request);
            getSubject(servletRequest, servletResponse).login(jwtPasswordToken);
        } catch (BusinessException exception) {
            throw exception;
        } catch (AuthenticationException e) {
            throw new BusinessException(BaseResponseCode.UNAUTHORIZED);
        } catch (Exception e) {
            throw new BusinessException(BaseResponseCode.SYSTEM_BUSY);
        }
        return true;
    }
}