package com.vladooha.epilepsycenterserviceappbackend.service.user.impl;

import com.vladooha.epilepsycenterserviceappbackend.model.user.UserRole;
import com.vladooha.epilepsycenterserviceappbackend.repo.user.UserRoleRepository;
import com.vladooha.epilepsycenterserviceappbackend.service.user.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserRole findByName(String name) {
        return userRoleRepository.findByName(name);
    }

    @Override
    public UserRole findById(Long id) {
        return userRoleRepository.getOne(id);
    }
}
