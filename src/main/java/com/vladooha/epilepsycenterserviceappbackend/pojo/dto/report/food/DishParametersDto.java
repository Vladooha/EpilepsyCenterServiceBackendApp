package com.vladooha.epilepsycenterserviceappbackend.pojo.dto.report.food;

import com.vladooha.epilepsycenterserviceappbackend.model.report.food.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode
public class DishParametersDto {
    public static final double DEFAULT_WEIGHT = 100.0;

    public static DishParametersDto fromDishParameters(DishParameters dishParameters) {
        return fromDishParameters(dishParameters, true);
    }

    public static DishParametersDto fromDishParameters(DishParameters dishParameters, boolean withDish) {
        DishParametersDto dishParametersDto = new DishParametersDto();

        dishParametersDto.setId(dishParameters.getId());

        if (withDish) {
            Dish dish = dishParameters.getDish();
            if (dish != null) {
                dishParametersDto.setDish(DishDto.fromDish(dish));
            }
        }

        dishParametersDto.setPercentage(dishParameters.getPercentage() != null
                ? dishParameters.getPercentage()
                : 0.0);
        dishParametersDto.setEating(dishParameters.getEating().toString());

        dishParametersDto.productWeightList = new ArrayList<>();
        if (dishParameters.getProductWeightMap() != null) {
            for (var entry: dishParameters.getProductWeightMap().entrySet()) {
                Product product = entry.getKey();
                Double weightDbl = entry.getValue();

                ProductDto productDto = ProductDto.fromProduct(product);
                double weight = weightDbl != null ? weightDbl : DEFAULT_WEIGHT;

                ProductWeightDto productWeightDto = new ProductWeightDto();
                productWeightDto.setProduct(productDto);
                productWeightDto.setWeight(weight);
                dishParametersDto.productWeightList.add(productWeightDto);
            }
        }

        return dishParametersDto;
    }

    private Long id;
    private DishDto dish;
    private double percentage;
    private String eating;
    private List<ProductWeightDto> productWeightList;
//    private Map<String, ProductDto> productIndexMap;
//    private Map<String, Double> weightIndexMap;
//    private Map<ProductDto, Double> productWeightMap;

    public DishParametersDto() { }

    public DishParameters toDishParameters() {
        return toDishParameters(true);
    }

    public DishParameters toDishParameters(boolean withDish) {
        DishParameters dishParameters = new DishParameters();

        if (withDish && dish != null) {
            dishParameters.setDish(dish.toDish());
        }

        dishParameters.setPercentage(percentage);
        try {
            dishParameters.setEating(Eating.valueOf(eating));
        } catch (Exception wrongEatingName) {
            dishParameters.setEating(null);
        }

        if (productWeightList != null) {
            Map<Product, Double> parameterProductWeightMap = new HashMap<>();
            for (ProductWeightDto productWeightDto : productWeightList) {
                parameterProductWeightMap.put(
                        productWeightDto.getProduct().toProduct(),
                        productWeightDto.getWeight()
                );
            }
            dishParameters.setProductWeightMap(parameterProductWeightMap);
        }

        return dishParameters;
    }
}
