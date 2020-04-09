package com.company.project.util;


import com.alibaba.fastjson.JSON;
import com.company.project.framework.holder.RequestHolder;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hyp
 * Project name is blog
 * Include in com.hyp.learn.core.utils
 * hyp create at 20-3-18
 **/
public class RequestUtil {

    private static final String STR_BODY = "body";


    public static String getParameters() {
        HttpServletRequest request = RequestHolder.getRequest();
        if (null == request) {
            return null;
        }
        Enumeration<String> paraNames = request.getParameterNames();
        if (paraNames == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        while (paraNames.hasMoreElements()) {
            String paraName = paraNames.nextElement();
            sb.append("&").append(paraName).append("=").append(request.getParameter(paraName));
        }
        return sb.toString();
    }

    public static Map<String, String[]> getParametersMap() {
        HttpServletRequest request = RequestHolder.getRequest();
        if (null == request) {
            return new HashMap<>();
        }
        return request.getParameterMap();
    }

    public static String getHeader(String headerName) {
        HttpServletRequest request = RequestHolder.getRequest();
        if (null == request) {
            return null;
        }
        return request.getHeader(headerName);
    }



    public static String getReferer() {
        return getHeader("Referer");
    }

    public static String getUa() {
        return getHeader("User-Agent");
    }

    public static String getIp() {
        HttpServletRequest request = RequestHolder.getRequest();
        if (null == request) {
            return null;
        }
        return IpUtil.getRealIp(request);
    }

    public static String getRequestUrl() {
        HttpServletRequest request = RequestHolder.getRequest();
        if (null == request) {
            return null;
        }
        return request.getRequestURL().toString();
    }

    public static String getMethod() {
        HttpServletRequest request = RequestHolder.getRequest();
        if (null == request) {
            return null;
        }
        return request.getMethod();
    }

    public static boolean isAjax(HttpServletRequest request) {
        if (null == request) {
            request = RequestHolder.getRequest();
        }
        if (null == request) {
            return false;
        }
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))
                || request.getParameter("ajax") != null;

    }


    public static Map<String, String> getRequestBodyMap(ServletRequest request) {
        Map<String ,String > dataMap = new HashMap<>(16);
        // 判断是否已经将 inputStream 流中的 body 数据读出放入 attribute
        if (request.getAttribute(STR_BODY) != null) {
            // 已经读出则返回attribute中的body
            return (Map<String,String>)request.getAttribute(STR_BODY);
        } else {
            try {
                Map<String, Object> maps = JSON.parseObject(request.getInputStream(),Map.class);
                maps.forEach((key, value) -> dataMap.put(key, String.valueOf(value)));
                request.setAttribute(STR_BODY,dataMap);
            }catch (IOException e) {
                e.printStackTrace();
            }
            return dataMap;
        }
    }

    /**
     * description 封装response  统一json返回
     *
     * @param outStr 1
     * @param response 2
     */
    public static void responseWrite(String outStr, ServletResponse response) {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter printWriter;
        try {
            printWriter = WebUtils.toHttp(response).getWriter();
            printWriter.write(outStr);
        }catch (Exception e) {
        }
    }
}
