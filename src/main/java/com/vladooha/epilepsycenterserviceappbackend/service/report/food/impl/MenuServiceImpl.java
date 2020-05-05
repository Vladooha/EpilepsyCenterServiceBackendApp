package com.vladooha.epilepsycenterserviceappbackend.service.report.food.impl;

import com.vladooha.epilepsycenterserviceappbackend.model.report.food.Menu;
import com.vladooha.epilepsycenterserviceappbackend.model.report.food.Product;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import com.vladooha.epilepsycenterserviceappbackend.repo.report.food.MenuRepository;
import com.vladooha.epilepsycenterserviceappbackend.service.report.food.MenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;

    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Menu findById(Long id) {
        return menuRepository.getOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Menu getActual(User patient) {
        return menuRepository.findFirstByOwnerOrderByDateDesc(patient);
    }

    @Override
    @Transactional
    public Menu merge(Menu menu) {
        Long id = menu.getId();
        if (null == id) {
            return save(menu);
        }

        Menu oldMenu = findById(id);
        if (null == oldMenu) {
            menu.setId(null);

            return save(menu);
        }

        if (oldMenu.getOwner().getId() != menu.getOwner().getId()) {
            return null;
        }

        oldMenu.setDishParametersList(menu.getDishParametersList());
        oldMenu.setDate(menu.getDate());

        return save(oldMenu);
    }

    @Override
    @Transactional
    public Menu save(Menu menu) {
        return menuRepository.save(menu);
    }
}
