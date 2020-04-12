package com.vladooha.epilepsycenterserviceappbackend.service.user;

import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import com.vladooha.epilepsycenterserviceappbackend.model.user.UserRole;

import java.util.List;

public interface UserService {
    User signUp(User user);

    User logIn(User user);

    User logOut(User user);

    User update(User user);

    List<User> findAll();

    User findById(Long id);

    User findByEmail(String email);

    List<User> findByRole(UserRole userRole);
}
