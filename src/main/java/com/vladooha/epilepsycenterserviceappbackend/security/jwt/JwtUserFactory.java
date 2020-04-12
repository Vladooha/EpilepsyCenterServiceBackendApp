package com.vladooha.epilepsycenterserviceappbackend.security.jwt;

import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import com.vladooha.epilepsycenterserviceappbackend.model.user.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getName(),
                user.getSurname(),
                user.getPhone(),
                mapToGrantedAuthorities(user.getUserRoles())
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<UserRole> userRoles) {
        return new ArrayList<>(userRoles)
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
