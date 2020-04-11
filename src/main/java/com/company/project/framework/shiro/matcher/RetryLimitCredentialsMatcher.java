package com.company.project.framework.shiro.matcher;

import com.company.project.business.consts.CacheConst;
import com.company.project.business.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Shiro-密码输入错误的状态下重试次数的匹配管理
 *
 * @author hyp
 * Project name is spring-boot-learn
 * Include in com.hyp.learn.shiro.business.shiro.credentials
 * hyp create at 20-3-29
 **/
@Slf4j
@Component
public class RetryLimitCredentialsMatcher extends PasswordMatcher {
    /**
     * 用户登录次数计数   redisKey 前缀
     */
    private static final String SHIRO_LOGIN_COUNT = CacheConst.SHIRO_LOGIN_COUNT;
    /**
     * 用户登录是否被锁定    30分钟 redisKey 前缀
     */
    private static final String SHIRO_IS_LOCK = CacheConst.SHIRO_IS_LOCK;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ISysUserService userService;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();
        // 访问一次，计数一次
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        String loginCountKey = SHIRO_LOGIN_COUNT + username;
        String isLockKey = SHIRO_IS_LOCK + username;

        if (redisTemplate.hasKey(isLockKey)) {
            throw new ExcessiveAttemptsException("帐号[" + username + "]已被禁止登录！");
        }
        // 计数大于5时，设置用户被锁定30分钟
        Integer loginCount = (Integer) ops.get(loginCountKey);
        loginCount = loginCount == null ? 0 : loginCount;
        int retryCount = (5 - loginCount);
        if (retryCount <= 0) {
            //30分钟
            ops.set(isLockKey, "Lock", 30, TimeUnit.MINUTES);
            redisTemplate.expire(loginCountKey, 30, TimeUnit.MINUTES);
            throw new ExcessiveAttemptsException("由于密码输入错误次数过多，帐号[" + username + "]已被禁止登录！");
        }

        boolean match = super.doCredentialsMatch(token, info);
        if (!match) {
            ops.increment(loginCountKey);
            String msg = retryCount <= 0 ? "您的账号30分钟内禁止登录！" : "您还剩" + retryCount + "次重试的机会";
            throw new AccountException("帐号或密码不正确！" + msg);
        } else {
            ops.set(loginCountKey, 0);
        }
        return true;
    }
}
