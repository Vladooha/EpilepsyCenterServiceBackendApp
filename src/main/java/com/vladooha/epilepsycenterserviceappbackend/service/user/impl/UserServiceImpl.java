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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Nullable
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    @Nullable
    @Transactional(readOnly = true)
    public User findByClinicId(String clinicId) {
        return userRepository.findByClinicId(clinicId);
    }

    @Override
    @Nullable
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findByRole(UserRole userRole) {
        return userRepository.findByUserRolesContaining(userRole);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findPatients(User doctor) { return  userRepository.findByDoctor(doctor); }

    @Override
    @Transactional(readOnly = true)
    public boolean isDoctor(User doctor) {
        List<UserRole> userRoles = doctor.getUserRoles();
        for (UserRole userRole : userRoles) {
            if (UserRole.DOCTOR.equals(userRole.getName())) {
                return true;
            }
        }

        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isDoctorsPatient(User doctor, User patient) {
        if (isDoctor(doctor)) {
            for (User doctorsPatient : doctor.getPatients()) {
                if (doctorsPatient.getId().equals(patient.getId())) {
                    return true;
                }
            }
        }

        return false;
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
    @Nullable
    @Transactional
    public User addPatient(User doctor, User patient) {
        if (patient != null) {
            if (isDoctor(patient)) {
                return null;
            }

            patient.setDoctor(doctor);
            doctor.getPatients().add(patient);
        }

        return patient;
    }
}
