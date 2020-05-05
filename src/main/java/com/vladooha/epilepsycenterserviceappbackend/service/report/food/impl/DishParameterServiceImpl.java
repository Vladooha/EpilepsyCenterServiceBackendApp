package com.vladooha.epilepsycenterserviceappbackend.service.report.food.impl;

import com.vladooha.epilepsycenterserviceappbackend.model.report.food.Dish;
import com.vladooha.epilepsycenterserviceappbackend.model.report.food.DishParameters;
import com.vladooha.epilepsycenterserviceappbackend.repo.report.food.DishParametersRepository;
import com.vladooha.epilepsycenterserviceappbackend.service.report.food.DishParameterService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DishParameterServiceImpl implements DishParameterService {
    private final DishParametersRepository dishParametersRepository;

    public DishParameterServiceImpl(DishParametersRepository dishParametersRepository) {
        this.dishParametersRepository = dishParametersRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public DishParameters findById(Long id) {
        return dishParametersRepository.getOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public DishParameters findLastDishParameters(Dish dish) {
        List<DishParameters> dishParameters =
            dishParametersRepository.findByDishOrderByDateDescTop1(dish, PageRequest.of(0, 1));

        if (null == dishParameters || dishParameters.isEmpty()) {
            return null;
        }

        return dishParameters.get(0);
    }

    @Override
    @Transactional
    public DishParameters merge(DishParameters dishParameters) {
        Long id = dishParameters.getId();
        if (null == id) {
            return save(dishParameters);
        }

        DishParameters oldDishParameters = findById(id);
        if (null == oldDishParameters) {
            dishParameters.setId(null);

            return save(dishParameters);
        }

        oldDishParameters.setDish(dishParameters.getDish());
        oldDishParameters.setProductWeightMap(dishParameters.getProductWeightMap());
        oldDishParameters.setEating(dishParameters.getEating());
        oldDishParameters.setPercentage(dishParameters.getPercentage());

        return save(oldDishParameters);
    }

    @Override
    @Transactional
    public DishParameters save(DishParameters dishParameters) {
        return dishParametersRepository.save(dishParameters);
    }
}
