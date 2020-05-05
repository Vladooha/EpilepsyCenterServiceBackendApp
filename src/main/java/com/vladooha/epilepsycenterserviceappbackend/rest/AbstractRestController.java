package com.vladooha.epilepsycenterserviceappbackend.rest;

import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import com.vladooha.epilepsycenterserviceappbackend.service.user.UserService;
import org.springframework.security.core.Authentication;

public class AbstractRestController {
    protected final UserService userService;

    public AbstractRestController(UserService userService) {
        this.userService = userService;
    }

    protected User getUserByAuth(Authentication authentication) {
        String email = authentication.getName();

        return userService.findByEmail(email);
    }
}
