package com.vladooha.epilepsycenterserviceappbackend.config;

import com.vladooha.epilepsycenterserviceappbackend.model.user.UserRole;
import com.vladooha.epilepsycenterserviceappbackend.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final static String API_ROOT = "/api";

    private final JwtTokenProvider jwtTokenProvider;

    private static final String[] UNLOGGED_ENDPOINTS = {
            "/h2-console/**",
            API_ROOT + "/user/sign-up",
            API_ROOT + "/user/log-in",
            API_ROOT + "/user/refresh-token",
    };
    private static final String[] ANY_ROLE_ENDPOINTS = {""};
    private static final String[] PATIENT_ENDPOINTS = {
            API_ROOT + "/user",
    };
    private static final String[] DOCTOR_ENDPOINTS = {""};
    private static final String[] ADMIN_ENDPOINTS = {""};

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin()
                .and()
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //.antMatchers(ANY_ROLE_ENDPOINTS).hasAnyRole(UserRole.PATIENT, UserRole.DOCTOR, UserRole.ADMIN)
                .antMatchers(PATIENT_ENDPOINTS).hasAuthority(UserRole.PATIENT)
                //.antMatchers(DOCTOR_ENDPOINTS).hasRole(UserRole.DOCTOR)
                //.antMatchers(ADMIN_ENDPOINTS).hasRole(UserRole.ADMIN)
                .antMatchers(UNLOGGED_ENDPOINTS).permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfig(jwtTokenProvider));
    }
}
