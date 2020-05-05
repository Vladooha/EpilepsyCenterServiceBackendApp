package com.vladooha.epilepsycenterserviceappbackend.service.report.food;

import com.vladooha.epilepsycenterserviceappbackend.model.report.food.Dish;
import com.vladooha.epilepsycenterserviceappbackend.model.report.food.DishParameters;

public interface DishParameterService {
    DishParameters findById(Long id);
    DishParameters findLastDishParameters(Dish dish);
    DishParameters merge(DishParameters dishParameters);
    DishParameters save(DishParameters dishParameters);
}
