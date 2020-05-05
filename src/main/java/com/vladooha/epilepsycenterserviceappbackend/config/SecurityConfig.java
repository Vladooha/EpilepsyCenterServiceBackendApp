package com.vladooha.epilepsycenterserviceappbackend.config;

import com.vladooha.epilepsycenterserviceappbackend.model.user.UserRole;
import com.vladooha.epilepsycenterserviceappbackend.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;

    private final String[] UNLOGGED_ENDPOINTS = {
            "/h2-console/**",
            "/users/**",
            "/report_test"
    };
    private final String[] ANY_ROLE_ENDPOINTS = {
            "/reports/**",
    };
    private final String[] PATIENT_ENDPOINTS = {
            "/food/**",
    };
    private final String[] DOCTOR_ENDPOINTS = {"" +
            "/doctors"
    };
    private final String[] ADMIN_ENDPOINTS = {""};

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
                .antMatchers(ANY_ROLE_ENDPOINTS).hasAnyAuthority(UserRole.PATIENT, UserRole.DOCTOR, UserRole.ADMIN)
                .antMatchers(PATIENT_ENDPOINTS).hasAuthority(UserRole.PATIENT)
                .antMatchers(DOCTOR_ENDPOINTS).hasAuthority(UserRole.DOCTOR)
//                .antMatchers(ADMIN_ENDPOINTS).hasAuthority(UserRole.ADMIN)
                .antMatchers(UNLOGGED_ENDPOINTS).permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfig(jwtTokenProvider));
    }
}
