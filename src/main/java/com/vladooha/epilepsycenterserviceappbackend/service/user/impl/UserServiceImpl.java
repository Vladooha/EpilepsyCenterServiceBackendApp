package com.vladooha.epilepsycenterserviceappbackend.service.user.impl;

import com.sun.istack.Nullable;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import com.vladooha.epilepsycenterserviceappbackend.model.user.UserRole;
import com.vladooha.epilepsycenterserviceappbackend.repo.user.UserRepository;
import com.vladooha.epilepsycenterserviceappbackend.repo.user.UserRoleRepository;
import com.vladooha.epilepsycenterserviceappbackend.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Nullable
    @Transactional
    public User signUp(User user) {
        if (user != null) {
            UserRole patientRole = userRoleRepository.findByName(UserRole.PATIENT);

            patientRole.getUsers().add(user);
            user.getUserRoles().add(patientRole);

            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

            return userRepository.save(user);
        }

        return null;
    }

    @Override
    @Transactional
    public User logIn(User user) {
        if (user != null) {
            String password = user.getPassword();
            User userFromDb = userRepository.findByEmail(user.getEmail());

            if (password != null && userFromDb != null) {
                if (bCryptPasswordEncoder.matches(password, userFromDb.getPassword())) {
                    return userFromDb;
                }
            }
        }

        return null;
    }

    @Override
    @Transactional
    public User logOut(User user) {
        return null;
    }

    @Override
    @Nullable
    @Transactional
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Nullable
    @Transactional
    public User findById(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    @Nullable
    @Transactional
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public List<User> findByRole(UserRole userRole) {
        return userRepository.findByUserRolesContaining(userRole);
    }
}
