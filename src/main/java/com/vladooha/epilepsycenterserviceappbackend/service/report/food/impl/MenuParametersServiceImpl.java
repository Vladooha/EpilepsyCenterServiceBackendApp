package com.vladooha.epilepsycenterserviceappbackend.service.report.food.impl;

import com.vladooha.epilepsycenterserviceappbackend.model.report.food.Menu;
import com.vladooha.epilepsycenterserviceappbackend.model.report.food.MenuParameters;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import com.vladooha.epilepsycenterserviceappbackend.repo.report.food.MenuParametersRepository;
import com.vladooha.epilepsycenterserviceappbackend.service.report.food.MenuParametersService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuParametersServiceImpl implements MenuParametersService {
    private MenuParametersRepository menuParametersRepository;

    public MenuParametersServiceImpl(MenuParametersRepository menuParametersRepository) {
        this.menuParametersRepository = menuParametersRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public MenuParameters findById(Long id) {
        return menuParametersRepository.getOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public MenuParameters findActualForPatient(User patient) {
        return menuParametersRepository.findFirstByPatientOrderByDateDesc(patient);
    }

    @Override
    @Transactional
    public MenuParameters save(MenuParameters menuParameters) {
        return menuParametersRepository.save(menuParameters);
    }
}
