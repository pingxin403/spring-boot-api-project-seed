package com.company.project.framework.shiro.token;

import org.apache.shiro.authc.AuthenticationToken;

public class PasswordToken implements AuthenticationToken {

    private static final long serialVersionUID = 1L;
    private String appId;
    private String password;
    private String timestamp;
    private String host;

    public PasswordToken(String appId, String password, String timestamp, String host) {
        this.appId = appId;
        this.timestamp = timestamp;
        this.host = host;
        this.password = password;

    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return this.password;
    }

    @Override
    public Object getPrincipal() {
        return this.appId;
    }

    @Override
    public Object getCredentials() {
        return this.password;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
