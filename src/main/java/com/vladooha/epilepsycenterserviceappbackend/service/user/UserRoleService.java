package com.vladooha.epilepsycenterserviceappbackend.service.user;

import com.vladooha.epilepsycenterserviceappbackend.model.user.UserRole;

public interface UserRoleService {
    UserRole findByName(String name);

    UserRole findById(Long id);
}
