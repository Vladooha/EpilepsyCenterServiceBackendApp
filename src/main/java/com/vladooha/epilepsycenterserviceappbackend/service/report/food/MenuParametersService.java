package com.vladooha.epilepsycenterserviceappbackend.service.report.food;

import com.vladooha.epilepsycenterserviceappbackend.model.report.food.MenuParameters;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;

public interface MenuParametersService {
    MenuParameters findById(Long id);
    MenuParameters findActualForPatient(User patient);
    MenuParameters save(MenuParameters menuParameters);
}
