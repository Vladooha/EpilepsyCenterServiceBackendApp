package com.vladooha.epilepsycenterserviceappbackend.service.report.food;

import com.vladooha.epilepsycenterserviceappbackend.model.report.food.Dish;
import com.vladooha.epilepsycenterserviceappbackend.model.report.food.Eating;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;

import java.util.List;

public interface DishService {
    Dish findById(Long id);
    List<Dish> getDishes(User owner);
    List<Dish> getDishes(User owner, Eating eating, String nameQuery);
    Dish merge(Dish dish);
    Dish save(Dish dish);
}
