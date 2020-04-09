package com.company.project.framework.shiro.provider;


import com.company.project.persistence.beans.SysUser;

/**
 * 数据库用户密码账户提供
 */
public interface AccountProvider {

    /**
     * description 数据库用户密码账户提供
     *
     * @param appId 1
     */
    SysUser loadAccount(Long appId);

}
