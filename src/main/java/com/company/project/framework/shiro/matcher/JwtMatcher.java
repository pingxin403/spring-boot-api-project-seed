package com.company.project.framework.shiro.matcher;


import com.company.project.business.consts.JwtConstant;
import com.company.project.business.vo.user.JwtAccount;
import com.company.project.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.springframework.stereotype.Component;


@Component
public class JwtMatcher implements CredentialsMatcher {


    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {

        String jwt = (String) authenticationInfo.getCredentials();
        JwtAccount jwtAccount = null;
        try {
            jwtAccount = JwtTokenUtil.parseJwt(jwt);
        } catch (SignatureException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            // 令牌错误
            throw new AuthenticationException(JwtConstant.ERR_JWT);
        } catch (ExpiredJwtException e) {
            // 令牌过期
            throw new AuthenticationException(JwtConstant.STR_EXPIRED);
        } catch (Exception e) {
            throw new AuthenticationException(JwtConstant.ERR_JWT);
        }
        if (null == jwtAccount) {
            throw new AuthenticationException(JwtConstant.ERR_JWT);
        }

        return true;
    }
}
