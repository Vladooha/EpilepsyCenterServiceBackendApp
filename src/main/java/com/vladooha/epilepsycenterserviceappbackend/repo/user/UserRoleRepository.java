package com.vladooha.epilepsycenterserviceappbackend.repo.user;

import com.vladooha.epilepsycenterserviceappbackend.model.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    UserRole findByName(String name);
}
