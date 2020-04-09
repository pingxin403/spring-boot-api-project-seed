package com.company.project.framework.shiro.token;

import com.company.project.business.consts.JwtConstant;
import com.company.project.util.RequestUtil;
import org.apache.shiro.authc.AuthenticationToken;

import javax.servlet.ServletRequest;


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

    @Override
    public Object getPrincipal() {
        return this.appId;
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

    public static JwtToken createJwtToken(ServletRequest request) {

        String appId = RequestUtil.getHeader(JwtConstant.APP_ID);
        String ipHost = request.getRemoteAddr();
        String jwt = RequestUtil.getHeader(JwtConstant.ACCESS_TOKEN);
        String deviceInfo = RequestUtil.getHeader("deviceInfo");

        return new JwtToken(ipHost, deviceInfo, jwt, appId);
    }
}
