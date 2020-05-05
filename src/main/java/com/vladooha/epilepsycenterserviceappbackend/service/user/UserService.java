package com.vladooha.epilepsycenterserviceappbackend.service.user;

import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import com.vladooha.epilepsycenterserviceappbackend.model.user.UserRole;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findByClinicId(String clinicId);
    User findById(Long id);
    User findByEmail(String email);
    List<User> findByRole(UserRole userRole);
    List<User> findPatients(User doctor);
    boolean isDoctor(User user);
    boolean isDoctorsPatient(User doctor, User patient);
    User signUp(User user);
    User logIn(User user);
    User logOut(User user);
    User update(User user);
    User addPatient(User doctor, User patient);
}
