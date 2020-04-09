package com.company.project.framework.filter;

import com.company.project.business.consts.JwtConstant;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 处理跨域请求
 *
 * @author hyp
 * Project name is spring-boot-api-project-seed
 * Include in com.company.project.framework.filter
 * hyp create at 20-4-5
 **/
public class CosFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest a = (HttpServletRequest) servletRequest;

        String origin = a.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, X-Requested-With, Content-Type, Accept, Cache-Control, Device, "
                        + JwtConstant.ACCESS_TOKEN + "," + JwtConstant.REFRESH_TOKEN);// jwt token和刷新token
        String method = a.getMethod();
        if ("OPTIONS".equalsIgnoreCase(method)) {
            servletResponse.getOutputStream().write("Success".getBytes(StandardCharsets.UTF_8));
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }

    @Override
    public void destroy() {
    }
}
