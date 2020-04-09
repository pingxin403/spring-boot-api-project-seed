package com.company.project.framework.shiro.provider.impl;


import com.company.project.business.service.ISysUserService;
import com.company.project.framework.shiro.provider.AccountProvider;
import com.company.project.persistence.beans.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("AccountProvider")
public class AccountProviderImpl implements AccountProvider {

    @Autowired
    private ISysUserService accountService;

    @Override
    public SysUser loadAccount(Long appId) {
        return accountService.getById(appId);
    }
}
