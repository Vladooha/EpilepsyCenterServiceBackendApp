package com.vladooha.epilepsycenterserviceappbackend.security.jwt;

import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import com.vladooha.epilepsycenterserviceappbackend.model.user.UserRole;
import com.vladooha.epilepsycenterserviceappbackend.service.user.UserService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {
    private static final String TOKEN_HEADER = "token-holder";
    private static final String TOKEN_TIMESTAMP_HEADER = "token-timestamp";
    public static final String REFRESH_TOKEN_HEADER = "refresh-token-holder";

    private class TokenPair {
        private final String token;
        private final String refreshToken;

        public TokenPair(String token, String refreshToken) {
            this.token = token;
            this.refreshToken = refreshToken;
        }

        public String getToken() {
            return token;
        }

        public String getRefreshToken() {
            return refreshToken;
        }
    }

    @Value("${jwt.token.secret}")
    private String secret;
    @Value("${jwt.token.expired}")
    private long validityInMilliseconds;
    @Value("${jwt.token.refresh.expired}")
    private long refreshValidityInMilliseconds;

    private final JwtEncoder jwtEncoder;
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    public JwtTokenProvider(JwtEncoder jwtEncoder, @Qualifier("jwtUserDetailsService") UserDetailsService userDetailsService, UserService userService) {
        this.jwtEncoder = jwtEncoder;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String parseAndRefreshResponseTokens(HttpServletRequest request, HttpServletResponse response) {
        JwtTokenMetainfo refreshTokenMetainfo = parseRefreshToken(request);

        if (refreshTokenMetainfo.getStatus() == JwtTokenStatus.VALID) {
            createResponseTokens(refreshTokenMetainfo.getEmail(), refreshTokenMetainfo.getRoles(), response);
        }

        return refreshTokenMetainfo.getEmail();
    }

    public void createResponseTokens(User user, HttpServletResponse response) {
        String email = user.getEmail();
        List<String> roleNames = convertRolesToNames(user.getUserRoles());

        createResponseTokens(email, roleNames, response);
    }

    public Authentication authenticate(HttpServletRequest request) {
        JwtTokenMetainfo jwtTokenMetainfo = parseToken(request);
        JwtTokenStatus status = jwtTokenMetainfo.getStatus();

        if (status == JwtTokenStatus.VALID) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwtTokenMetainfo.getEmail());
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        }

        return null;
    }

    private List<String> convertRolesToNames(List<UserRole> userRoles) {
        List<String> result = new ArrayList<>();

        userRoles.forEach(role -> {
            result.add(role.getName());
        });

        return result;
    }

    private void createResponseTokens(String email, List<String> roleNames, HttpServletResponse response) {
        TokenPair tokenPair = createTokenPair(email, roleNames);

        addTokenPairToResponse(tokenPair, response);
    }

    private TokenPair createTokenPair(String email, List<String> userRoles) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put(JwtTokenMetainfo.ROLES_CLAIMS, userRoles);

        Date now = new Date();
        Date tokenValidity = new Date(now.getTime() + validityInMilliseconds);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        Claims refreshClaims = Jwts.claims().setSubject(email);
        refreshClaims.put(JwtTokenMetainfo.ROLES_CLAIMS, userRoles);

        now = new Date();
        Date refreshTokenValidity = new Date(now.getTime() + refreshValidityInMilliseconds);

        String refreshToken = Jwts.builder()
                .setClaims(refreshClaims)
                .setIssuedAt(now)
                .setExpiration(refreshTokenValidity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        return new TokenPair(token, refreshToken);
    }

    private void addTokenPairToResponse(TokenPair tokenPair, HttpServletResponse response) {
        Long currentTime = new Date().getTime();

        response.setHeader(TOKEN_HEADER, tokenPair.token);
        response.setHeader(REFRESH_TOKEN_HEADER, tokenPair.refreshToken);
        response.setHeader(TOKEN_TIMESTAMP_HEADER, currentTime.toString());
    }

    private JwtTokenMetainfo parseToken(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);
        return JwtTokenMetainfo.fromToken(token);
    }

    private JwtTokenMetainfo parseRefreshToken(HttpServletRequest request) {
        String token = request.getHeader(REFRESH_TOKEN_HEADER);
        return JwtTokenMetainfo.fromRefreshToken(token);
    }
}
