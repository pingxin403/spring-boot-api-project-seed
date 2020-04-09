package com.company.project.framework.shiro.realm;


import com.company.project.framework.shiro.matcher.JwtMatcher;
import com.company.project.framework.shiro.matcher.RetryLimitCredentialsMatcher;
import com.company.project.framework.shiro.provider.AccountProvider;
import com.company.project.framework.shiro.token.JwtToken;
import com.company.project.framework.shiro.token.PasswordToken;
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
    private JwtMatcher jwtMatcher;
    @Autowired
    private AccountProvider accountProvider;


    public List<Realm> initGetRealm() {
        List<Realm> realmList = new LinkedList<>();
        // ----- password
        PasswordRealm passwordRealm = new PasswordRealm();
        passwordRealm.setAccountProvider(accountProvider);
        passwordRealm.setCredentialsMatcher(limitCredentialsMatcher);
        passwordRealm.setAuthenticationTokenClass(PasswordToken.class);
        realmList.add(passwordRealm);
        // ----- jwt
        JwtRealm jwtRealm = new JwtRealm();
        jwtRealm.setCredentialsMatcher(jwtMatcher);
        jwtRealm.setAuthenticationTokenClass(JwtToken.class);
        realmList.add(jwtRealm);
        return Collections.unmodifiableList(realmList);
    }
}
