package com.vladooha.epilepsycenterserviceappbackend.rest.report.food;

import com.vladooha.epilepsycenterserviceappbackend.model.report.food.*;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import com.vladooha.epilepsycenterserviceappbackend.pojo.dto.report.food.DishDto;
import com.vladooha.epilepsycenterserviceappbackend.pojo.dto.report.food.ProductDto;
import com.vladooha.epilepsycenterserviceappbackend.rest.AbstractRestController;
import com.vladooha.epilepsycenterserviceappbackend.service.report.food.DishParameterService;
import com.vladooha.epilepsycenterserviceappbackend.service.report.food.DishService;
import com.vladooha.epilepsycenterserviceappbackend.service.report.food.ProductService;
import com.vladooha.epilepsycenterserviceappbackend.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
public class FoodRestController extends AbstractRestController {
    private final DishService dishService;
    private final ProductService productService;

    public FoodRestController(UserService userService, DishService dishService, DishParameterService dishParameterService, ProductService productService) {
        super(userService);
        this.dishService = dishService;
        this.productService = productService;
    }

    @GetMapping("/food/dishes")
    public ResponseEntity getDishes(
            @RequestParam(value = "nameQuery", required = false, defaultValue = "") String nameQuery,
            @RequestParam(value = "eating", required = false) String eatingStr,
            Authentication authentication
    ) {
        User user = getUserByAuth(authentication);
        if (null == user) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        Eating eating = parseEating(eatingStr);
        List<Dish> dishes = dishService.getDishes(user, eating, nameQuery);
        if (null == dishes || dishes.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

        List<DishDto> productDtoList = dishes.stream()
                .map((dish) -> DishDto.fromDish(dish))
                .collect(Collectors.toList());

        return new ResponseEntity(productDtoList, HttpStatus.OK);
    }

    @GetMapping("/food/products")
    public ResponseEntity getProducts(
            @RequestParam(value = "nameQuery", required = false, defaultValue = "") String nameQuery,
            @RequestParam(value = "productTypes", required = false) List<String> productTypesStr,
            Authentication authentication
    ) {
        System.out.println("Query: " + nameQuery);

        User user = getUserByAuth(authentication);
        if (null == user) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        List<ProductType> productTypes = parseProductTypes(productTypesStr);
        List<Product> products = productService.getProducts(user, productTypes, nameQuery);

        if (null == products || products.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

        List<ProductDto> productDtoList = products.stream()
                .map((product) -> ProductDto.fromProduct(product))
                .collect(Collectors.toList());

        return new ResponseEntity(productDtoList, HttpStatus.OK);
    }

    @PostMapping("/food/products")
    public ResponseEntity postProduct(@RequestBody ProductDto productDto,
                                      Authentication authentication) {
        User user = getUserByAuth(authentication);
        if (null == user) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        Product product = productDto.toProduct();
        product.setOwner(user);
        product = productService.merge(product);
        if (null == product) {
            return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity(ProductDto.fromProduct(product), HttpStatus.CREATED);
    }

    private Eating parseEating(String eatingStr) {
        try {
            return Eating.valueOf(eatingStr);
        } catch (Exception wrongEatingName) {
            return null;
        }
    }

    private List<ProductType> parseProductTypes(List<String> productTypesStr) {
        if (null == productTypesStr) {
            return null;
        }

        List<ProductType> productTypes = new ArrayList<>();
        productTypesStr.forEach((singleProductTypeStr) -> {
            try {
                productTypes.add(ProductType.valueOf(singleProductTypeStr));
            } catch (Exception wrongTypeName) { }
        });

        return productTypes;
    }
}
