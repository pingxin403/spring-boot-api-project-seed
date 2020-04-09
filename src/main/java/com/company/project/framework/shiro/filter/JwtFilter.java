package com.company.project.framework.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.company.project.business.consts.JwtConstant;
import com.company.project.business.enums.PlatformEnum;
import com.company.project.business.service.ISysLogService;
import com.company.project.business.service.ISysUserService;
import com.company.project.business.service.RedisService;
import com.company.project.framework.exception.BusinessException;
import com.company.project.framework.exception.code.BaseResponseCode;
import com.company.project.framework.shiro.token.JwtToken;
import com.company.project.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

/**
 * 支持restful url 的过滤链  JWT json web token 过滤器，无状态验证
 *
 * @author hyp
 * Project name is spring-boot-api-project-seed
 * Include in com.company.project.framework.shiro.filter
 * hyp create at 20-4-8
 **/
@Slf4j
public class JwtFilter extends AbstractPathMatchingFilter {

    private static final String STR_EXPIRED = JwtConstant.STR_EXPIRED;

    private RedisService redisService;
    private ISysUserService userService;


    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object mappedValue) throws Exception {
        Subject subject = getSubject(servletRequest, servletResponse);
        boolean isJwtPost = (null != subject && !subject.isAuthenticated()) && isJwtSubmission(servletRequest);
        // 判断是否为JWT认证请求
        if (isJwtPost) {
            AuthenticationToken token = createJwtToken(servletRequest);
            try {
                subject.login(token);
                return this.checkRoles(subject,mappedValue);
            }catch (AuthenticationException e) {
                // 如果是JWT过期
                if (STR_EXPIRED.equals(e.getMessage())) {
                    // 这里初始方案先抛出令牌过期，之后设计为在Redis中查询当前appId对应令牌，其设置的过期时间是JWT的两倍，此作为JWT的refresh时间
                    // 当JWT的有效时间过期后，查询其refresh时间，refresh时间有效即重新派发新的JWT给客户端，
                    // refresh也过期则告知客户端JWT时间过期重新认证

                    // 当存储在redis的JWT没有过期，即refresh time 没有过期
                    String appId = RequestUtil.getHeader(JwtConstant.APP_ID);
                    String jwt = RequestUtil.getHeader(JwtConstant.ACCESS_TOKEN);
                }
            }
        }

        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        Subject subject = getSubject(servletRequest, servletResponse);

        // 未认证的情况上面已经处理  这里处理未授权
        if (subject != null && subject.isAuthenticated()) {
            //  已经认证但未授权的情况
            // 告知客户端JWT没有权限访问此资源
            throw new BusinessException(BaseResponseCode.FORBIDDEN);
        }
        // 过滤链终止
        return false;
    }


    private boolean isJwtSubmission(ServletRequest request) {

        String jwt = RequestUtil.getHeader(JwtConstant.ACCESS_TOKEN);
        String appId = RequestUtil.getHeader(JwtConstant.APP_ID);
        return (request instanceof HttpServletRequest)
                && !StringUtils.isEmpty(jwt)
                && !StringUtils.isEmpty(appId);
    }

    private AuthenticationToken createJwtToken(ServletRequest request) {
        String appId = RequestUtil.getHeader(JwtConstant.APP_ID);
        String jwt = RequestUtil.getHeader(JwtConstant.ACCESS_TOKEN);
        String ip = RequestUtil.getIp();
        String deviceInfo = RequestUtil.getHeader(JwtConstant.DEVICE_INFO);

        JwtToken token = new JwtToken(ip, deviceInfo, jwt, appId);

        return token;
    }

    /**
     * description 验证当前用户是否属于mappedValue任意一个角色
     *
     * @param subject     1
     * @param mappedValue 2
     * @return boolean
     */
    private boolean checkRoles(Subject subject, Object mappedValue) {
        String[] rolesArray = (String[]) mappedValue;
        return rolesArray == null || rolesArray.length == 0 || Stream.of(rolesArray).anyMatch(role -> subject.hasRole(role.trim()));
    }

    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    public void setUserService(ISysUserService userService) {
        this.userService = userService;
    }

}