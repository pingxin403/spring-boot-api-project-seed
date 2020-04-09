package com.company.project.util;


import com.company.project.business.consts.JwtConstant;
import com.company.project.business.vo.user.JwtAccount;
import com.company.project.framework.property.JwtProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Slf4j
public class JwtTokenUtil {

    private static String secretKey;
    private static Duration accessTokenExpireTime;
    private static Duration refreshTokenExpireTime;
    private static Duration refreshTokenExpireAppTime;
    private static String issuer;

    public static void setTokenSettings(JwtProperties jwtProperties) {
        secretKey = jwtProperties.getSecretKey();
        accessTokenExpireTime = jwtProperties.getAccessTokenExpireTime();
        refreshTokenExpireTime = jwtProperties.getRefreshTokenExpireTime();
        refreshTokenExpireAppTime = jwtProperties.getRefreshTokenExpireAppTime();
        issuer = jwtProperties.getIssuer();
    }

    /**
     * 生成 access_token
     */
    public static String getAccessToken(String subject, Map<String, Object> claims) {

        return generateToken(issuer, subject, claims, accessTokenExpireTime.toMillis(), secretKey);
    }

    /**
     * 生产 App端 refresh_token
     */
    public static String getRefreshAppToken(String subject, Map<String, Object> claims) {
        return generateToken(issuer, subject, claims, refreshTokenExpireAppTime.toMillis(), secretKey);
    }

    /**
     * 生产 PC refresh_token
     */
    public static String getRefreshToken(String subject, Map<String, Object> claims) {
        return generateToken(issuer, subject, claims, refreshTokenExpireTime.toMillis(), secretKey);
    }

    /**
     * 签发token
     *
     * @param issuer    签发人
     * @param subject   代表这个JWT的主体，即它的所有人 一般是用户id
     * @param claims    存储在JWT里面的信息 一般放些用户的权限/角色信息
     * @param ttlMillis 有效时间(毫秒)
     * @return java.lang.String
     * @throws
     */
    public static String generateToken(String issuer, String subject, Map<String, Object> claims, long ttlMillis, String secret) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] signingKey = DatatypeConverter.parseBase64Binary(secret);

        JwtBuilder builder = Jwts.builder();
        if (null != claims) {
            builder.setClaims(claims);
        }
        if (!StringUtils.isEmpty(subject)) {
            builder.setSubject(subject);
        }
        if (!StringUtils.isEmpty(issuer)) {
            builder.setIssuer(issuer);
        }
        // 设置签发时间
        builder.setIssuedAt(now);
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        // 压缩，可选GZIP
        builder.compressWith(CompressionCodecs.DEFLATE);
        // 加密设置
        builder.signWith(signatureAlgorithm, signingKey);
        return builder.compact();
    }

    /**
     * 获取用户id
     */
    public static String getUserId(String token) {
        String userId = null;
        try {
            Claims claims = getClaimsFromToken(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            log.error("eror={}", e);
        }
        return userId;
    }

    /**
     * 获取用户名
     */
    public static String getUserName(String token) {

        String username = null;
        try {
            Claims claims = getClaimsFromToken(token);
            username = (String) claims.get(JwtConstant.JWT_USER_NAME);
        } catch (Exception e) {
            log.error("eror={}", e);
        }
        return username;
    }

    /**
     * 从令牌中获取数据声明
     */
    public static Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey)).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 校验令牌
     */
    public static Boolean validateToken(String token) {
        Claims claimsFromToken = getClaimsFromToken(token);
        return (null != claimsFromToken && !isTokenExpired(token));
    }

    /**
     * 验证token 是否过期
     */
    public static Boolean isTokenExpired(String token) {

        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            log.error("error={}", e);
            return true;
        }
    }

    /**
     * 刷新token
     *
     * @param refreshToken
     * @param claims       主动去刷新的时候 改变JWT payload 内的信息
     */
    public static String refreshToken(String refreshToken, Map<String, Object> claims) {
        String refreshedToken;
        try {
            Claims parserclaims = getClaimsFromToken(refreshToken);
            /**
             * 刷新token的时候如果为空说明原先的 用户信息不变 所以就引用上个token里的内容
             */
            if (null == claims) {
                claims = parserclaims;
            }
            refreshedToken = generateToken(parserclaims.getIssuer(), parserclaims.getSubject(), claims, accessTokenExpireTime.toMillis(), secretKey);
        } catch (Exception e) {
            refreshedToken = null;
            log.error("error={}", e);
        }
        return refreshedToken;
    }

    /**
     * 获取token的剩余过期时间
     */
    public static long getRemainingTime(String token) {
        long result = 0;
        try {
            long nowMillis = System.currentTimeMillis();
            result = getClaimsFromToken(token).getExpiration().getTime() - nowMillis;
        } catch (Exception e) {
            log.error("error={}", e);
        }
        return result;
    }

    /**
     * 验签JWT
     *
     * @param jwt json web token
     */
    public static JwtAccount parseJwt(String jwt) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                .parseClaimsJws(jwt)
                .getBody();
        JwtAccount jwtAccount = new JwtAccount();
        //令牌ID
        jwtAccount.setTokenId(claims.getId());
        // 客户标识
        jwtAccount.setAppId(claims.getSubject());
        // 签发者
        jwtAccount.setIssuer(claims.getIssuer());
        // 签发时间
        jwtAccount.setIssuedAt(claims.getIssuedAt());
        // 接收方
        jwtAccount.setAudience(claims.getAudience());
        // 访问主张-角色
        jwtAccount.setRoles(claims.get("roles", String.class));
        // 访问主张-权限
        jwtAccount.setPerms(claims.get("perms", String.class));
        return jwtAccount;
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * description 从json数据中读取格式化map
     *
     * @param val 1
     * @return java.util.Map<java.lang.String, java.lang.Object>
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> readValue(String val) {
        try {
            return MAPPER.readValue(val, Map.class);
        } catch (IOException e) {
            throw new MalformedJwtException("Unable to read JSON value: " + val, e);
        }
    }

    /**
     * 分割字符串进SET
     */
    @SuppressWarnings("unchecked")
    public static Set<String> split(String str) {

        Set<String> set = new HashSet<>();
        if (StringUtils.isEmpty(str)) {
            return set;
        }
        set.addAll(CollectionUtils.arrayToList(str.split(",")));
        return set;
    }
}
