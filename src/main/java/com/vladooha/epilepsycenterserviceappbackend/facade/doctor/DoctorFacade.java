package com.vladooha.epilepsycenterserviceappbackend.facade.doctor;

import com.vladooha.epilepsycenterserviceappbackend.model.report.food.MenuParameters;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import com.vladooha.epilepsycenterserviceappbackend.service.report.food.MenuParametersService;
import com.vladooha.epilepsycenterserviceappbackend.service.user.UserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class DoctorFacade {
    private final UserService userService;
    private final MenuParametersService menuParametersService;

    public DoctorFacade(UserService userService, MenuParametersService menuParametersService) {
        this.userService = userService;
        this.menuParametersService = menuParametersService;
    }

    @Transactional
    public MenuParameters createNewMenuParameters(MenuParameters menuParameters, User doctor, User patient) {
        if (userService.isDoctor(doctor) && userService.isDoctorsPatient(doctor, patient)) {
            menuParameters.setDoctor(doctor);
            menuParameters.setPatient(patient);
            menuParameters.setDate(LocalDateTime.now());

            MenuParameters savedMenuParameters = menuParametersService.save(menuParameters);

            return savedMenuParameters;
        }

        return null;
    }

    @Transactional
    public MenuParameters getActualMenuParameters(User doctor, User patient) {
        if (userService.isDoctorsPatient(doctor, patient)) {
            return menuParametersService.findActualForPatient(patient);
        }

        return null;
    }
}
