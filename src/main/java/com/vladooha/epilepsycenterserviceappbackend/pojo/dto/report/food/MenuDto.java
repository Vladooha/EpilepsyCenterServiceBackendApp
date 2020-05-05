package com.vladooha.epilepsycenterserviceappbackend.pojo.dto.report.food;

import com.vladooha.epilepsycenterserviceappbackend.model.report.food.DishParameters;
import com.vladooha.epilepsycenterserviceappbackend.model.report.food.Menu;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class MenuDto {
    public static MenuDto fromMenu(Menu menu) {
        MenuDto menuDto = new MenuDto();

        menuDto.setId(menu.getId());

        List<DishParametersDto> parametersDtos = new ArrayList<>();
        menu.getDishParametersList()
            .forEach((dishParameters) -> {
                parametersDtos.add(DishParametersDto.fromDishParameters(dishParameters));
            });
        menuDto.setDishParametersList(parametersDtos);

        return menuDto;
    }

    private Long id;
    private List<DishParametersDto> dishParametersList;

    public MenuDto() { }

    public Menu toMenu() {
        Menu menu = new Menu();

        menu.setId(id);

        if (dishParametersList != null) {
            List<DishParameters> dishParameters = new ArrayList<>();
            dishParametersList.forEach((dishParametersDto) -> {
                dishParameters.add(dishParametersDto.toDishParameters());
            });
            menu.setDishParametersList(dishParameters);
        }

        return menu;
    }
}
