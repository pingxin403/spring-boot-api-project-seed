package com.company.project.framework.shiro.realm;


import com.company.project.business.service.ISysUserService;
import com.company.project.framework.shiro.matcher.ShiroHashedCredentialsMatcher;
import com.company.project.framework.shiro.matcher.RetryLimitCredentialsMatcher;
import com.company.project.framework.shiro.token.JwtToken;
import org.apache.shiro.realm.Realm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * realm管理器
 */
@Component
public class RealmManager {

    @Autowired
    private RetryLimitCredentialsMatcher limitCredentialsMatcher;
    @Autowired
    private ShiroHashedCredentialsMatcher shiroHashedCredentialsMatcher;
    @Autowired
    private ISysUserService accountProvider;


    public List<Realm> initGetRealm() {
        List<Realm> realmList = new LinkedList<>();
        // ----- jwt
        JwtRealm jwtRealm = new JwtRealm();
        jwtRealm.setCredentialsMatcher(shiroHashedCredentialsMatcher);
        jwtRealm.setAuthenticationTokenClass(JwtToken.class);
        realmList.add(jwtRealm);
        return Collections.unmodifiableList(realmList);
    }
}
