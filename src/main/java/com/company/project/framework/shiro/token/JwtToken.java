package com.company.project.framework.shiro.token;

import com.company.project.business.consts.JwtConstant;
import org.apache.shiro.authc.AuthenticationToken;

import javax.servlet.http.HttpServletRequest;


/**
 * JWT token
 */
public class JwtToken implements AuthenticationToken {

    private static final long serialVersionUID = 1L;

    /**
     * 用户的标识
     */
    private String appId;
    /**
     * 用户的IP
     */
    private String ipHost;
    /**
     * 设备信息
     */
    private String deviceInfo;

    /**
     * json web token值
     */
    private String token;

    public JwtToken(String ipHost, String deviceInfo, String jwt, String appId) {
        this.ipHost = ipHost;
        this.deviceInfo = deviceInfo;
        this.token = jwt;
        this.appId = appId;
    }

    public static JwtToken createJwtToken(HttpServletRequest request) {

        String appId = request.getHeader(JwtConstant.APP_ID);
        String ipHost = request.getRemoteAddr();
        String jwt = request.getHeader(JwtConstant.ACCESS_TOKEN);
        String deviceInfo = request.getHeader("deviceInfo");

        return new JwtToken(ipHost, deviceInfo, jwt, appId);
    }

    @Override
    public Object getPrincipal() {
        return this.token;
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }

    public String getIpHost() {
        return ipHost;
    }

    public void setIpHost(String ipHost) {
        this.ipHost = ipHost;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
