package com.vladooha.epilepsycenterserviceappbackend.service.report.food.impl;

import com.vladooha.epilepsycenterserviceappbackend.model.report.food.Dish;
import com.vladooha.epilepsycenterserviceappbackend.model.report.food.DishParameters;
import com.vladooha.epilepsycenterserviceappbackend.model.report.food.Eating;
import com.vladooha.epilepsycenterserviceappbackend.model.report.food.Product;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import com.vladooha.epilepsycenterserviceappbackend.pojo.dto.report.food.ProductDto;
import com.vladooha.epilepsycenterserviceappbackend.repo.report.food.DishRepository;
import com.vladooha.epilepsycenterserviceappbackend.service.report.food.DishService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DishServiceImpl implements DishService {
    final private DishRepository dishRepository;

    public DishServiceImpl(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Dish findById(Long id) {
        return dishRepository.getOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Dish> getDishes(User owner) {
        return dishRepository.findAllByOwner(owner);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Dish> getDishes(User owner, Eating eating, String nameQuery) {
        if (null == eating) {
            return getDishes(owner);
        }

        if (null == nameQuery) {
            nameQuery = "";
        }

        return dishRepository.findAllByOwnerAndEatingAndNameContains(owner, eating, nameQuery);
    }

    @Override
    @Transactional
    public Dish merge(Dish dish) {
        Long id = dish.getId();
        if (null == id) {
            return save(dish);
        }

        Dish oldDish = findById(id);
        if (null == oldDish) {
            dish.setId(null);

            return save(dish);
        }

        if (oldDish.getOwner().getId() != dish.getOwner().getId()) {
            return null;
        }

        oldDish.setName(dish.getName());
        oldDish.setProductList(dish.getProductList());

        return save(oldDish);
    }

    @Override
    @Transactional
    public Dish save(Dish dish) {
        return dishRepository.save(dish);
    }
}
