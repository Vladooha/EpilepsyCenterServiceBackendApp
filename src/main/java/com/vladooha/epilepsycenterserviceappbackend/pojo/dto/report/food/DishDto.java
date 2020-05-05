package com.vladooha.epilepsycenterserviceappbackend.pojo.dto.report.food;

import com.vladooha.epilepsycenterserviceappbackend.model.report.food.Dish;
import com.vladooha.epilepsycenterserviceappbackend.model.report.food.DishParameters;
import com.vladooha.epilepsycenterserviceappbackend.model.report.food.Product;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
public class DishDto {
    public static DishDto fromDish(Dish dish) {
        DishDto dishDto = new DishDto();

        dishDto.setId(dish.getId());

        dishDto.setName(dish.getName());

        DishParameters dishParameters = dish.getLastDishParameters();
        if (dishParameters != null) {
            DishParametersDto dishParametersDto = DishParametersDto.fromDishParameters(dishParameters, false);
            dishDto.setLastDishParameters(dishParametersDto);
        }

        return dishDto;
    }

    private Long id;
    private String name;
    private DishParametersDto lastDishParameters;

    public Dish toDish() {
        Dish dish = new Dish();

        dish.setId(id);

        dish.setName(name);

        List<Product> products = new ArrayList<>();
        dish.setProductList(products);

        if (lastDishParameters != null) {
            dish.setLastDishParameters(lastDishParameters.toDishParameters());
        }

        return dish;
    }
}
