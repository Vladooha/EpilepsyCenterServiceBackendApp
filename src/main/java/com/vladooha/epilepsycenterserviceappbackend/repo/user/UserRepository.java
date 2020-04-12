package com.vladooha.epilepsycenterserviceappbackend.repo.user;

import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import com.vladooha.epilepsycenterserviceappbackend.model.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByPhone(String phone);
    List<User> findByUserRolesContaining(UserRole userRole);
}
