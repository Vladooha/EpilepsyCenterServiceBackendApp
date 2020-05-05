package com.vladooha.epilepsycenterserviceappbackend.service.report.food;

import com.vladooha.epilepsycenterserviceappbackend.model.report.food.Menu;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;

public interface MenuService {
    Menu findById(Long id);
    Menu getActual(User patient);
    Menu merge(Menu menu);
    Menu save(Menu menu);
}
