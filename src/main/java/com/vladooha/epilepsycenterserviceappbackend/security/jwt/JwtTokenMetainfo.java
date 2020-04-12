package com.vladooha.epilepsycenterserviceappbackend.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.List;

enum JwtTokenStatus {
    VALID,
    INVALID,
    EXPIRED
}

@Component
public class JwtTokenMetainfo {
    public static final String ROLES_CLAIMS = "roles";

    private static String tokenSecret;

    private String token;
    private boolean isRefreshToken;
    private JwtTokenStatus status;
    private String email;
    private List<String> roles;

    @Value("${jwt.token.secret}")
    public void setTokenSecret(String tokenSecret) {
        JwtTokenMetainfo.tokenSecret = tokenSecret;
    }

    public static JwtTokenMetainfo fromToken(String token) {
        JwtTokenMetainfo tokenMetainfo = parseTokenBySecret(token, tokenSecret);
        tokenMetainfo.isRefreshToken = false;

        return tokenMetainfo;
    }

    public static JwtTokenMetainfo fromRefreshToken(String refreshToken) {
        JwtTokenMetainfo tokenMetainfo = parseTokenBySecret(refreshToken, tokenSecret);
        tokenMetainfo.isRefreshToken = true;

        return tokenMetainfo;
    }

    private static JwtTokenMetainfo parseTokenBySecret(String token, String secret) {
        JwtTokenMetainfo jwtTokenMetainfo = new JwtTokenMetainfo();
        jwtTokenMetainfo.token = token;

        try {
            Jws<Claims> claimsJws = Jwts
                    .parser()
                    .setSigningKey(secret.getBytes(Charset.forName("UTF-8")))
                    //.setSigningKey(secret)
                    .parseClaimsJws(token);
            jwtTokenMetainfo.email = claimsJws.getBody().getSubject();
            jwtTokenMetainfo.roles = claimsJws.getBody().get(ROLES_CLAIMS, List.class);
            jwtTokenMetainfo.status = JwtTokenStatus.VALID;
        } catch (ExpiredJwtException e) {
            Claims claims = e.getClaims();
            jwtTokenMetainfo.email = claims.getSubject();
            jwtTokenMetainfo.roles = claims.get(ROLES_CLAIMS, List.class);
            jwtTokenMetainfo.status = JwtTokenStatus.EXPIRED;
        } catch (Exception e) {
            jwtTokenMetainfo.email = null;
            jwtTokenMetainfo.roles = null;
            jwtTokenMetainfo.status = JwtTokenStatus.INVALID;
        }

        return jwtTokenMetainfo;
    }

    private JwtTokenMetainfo() {}

    public JwtTokenStatus getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public boolean isRefreshToken() {
        return isRefreshToken;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }
}
