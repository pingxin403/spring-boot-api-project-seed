package com.company.project.framework.shiro.matcher;


import com.company.project.business.consts.JwtConstant;
import com.company.project.business.service.RedisService;
import com.company.project.business.vo.user.JwtAccount;
import com.company.project.framework.exception.BusinessException;
import com.company.project.framework.exception.code.BaseResponseCode;
import com.company.project.framework.shiro.token.JwtToken;
import com.company.project.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ShiroHashedCredentialsMatcher implements CredentialsMatcher {

    @Autowired
    private RedisService redisService;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo authenticationInfo) {
        JwtToken jwtToken = (JwtToken) token;
        String accessToken = (String) jwtToken.getPrincipal();
        String userId = JwtTokenUtil.getUserId(accessToken);
        if (null == userId) {
            throw new AuthenticationException(JwtConstant.ERR_JWT);
        }
        if (redisService.hasKey(JwtConstant.ACCOUNT_LOCK_KEY + userId)) {
            throw new BusinessException(BaseResponseCode.ACCOUNT_LOCK_ERROR);
        }
        if (redisService.hasKey(JwtConstant.DELETED_USER_KEY + userId)) {
            throw new BusinessException(BaseResponseCode.ACCOUNT_ERROR);
        }
        if (redisService.hasKey(JwtConstant.JWT_REFRESH_TOKEN_BLACKLIST + accessToken)) {
            throw new BusinessException(BaseResponseCode.TOKEN_ERROR);
        }
        if (redisService.hasKey(JwtConstant.JWT_REFRESH_STATUS + accessToken)) {
            return true;
        }
        if (JwtTokenUtil.isTokenExpired(accessToken)) {
            throw new BusinessException(BaseResponseCode.TOKEN_PAST_DUE);
        }
        if (redisService.hasKey(JwtConstant.JWT_REFRESH_KEY + userId) && redisService.getExpire(JwtConstant.JWT_REFRESH_KEY + userId) > JwtTokenUtil.getRemainingTime(accessToken)) {
            if (!redisService.hasKey(JwtConstant.JWT_REFRESH_IDENTIFICATION + accessToken)) {
                throw new BusinessException(BaseResponseCode.TOKEN_PAST_DUE);
            }
        }

        return true;
    }
}
