package com.vladooha.epilepsycenterserviceappbackend.pojo.dto.report.food;

import com.vladooha.epilepsycenterserviceappbackend.model.report.food.Dish;
import com.vladooha.epilepsycenterserviceappbackend.model.report.food.Product;
import com.vladooha.epilepsycenterserviceappbackend.model.report.food.ProductType;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.parameters.P;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class ProductDto {
    public static ProductDto fromProduct(Product product) {
        ProductDto productDto = new ProductDto();

        productDto.setId(product.getId());
        productDto.setOwnerId(product.getOwner().getId());

        productDto.setName(product.getName());
        productDto.setType(product.getType() != null ? product.getType().toString() : null);

        productDto.setProteins(product.getProteins() != null ? product.getProteins() : 0.0);
        productDto.setFats(product.getFats() != null ? product.getFats() : 0.0);
        productDto.setCarbohydrates(product.getCarbohydrates() != null
                ? product.getCarbohydrates()
                : 0.0);

        return productDto;
    }

    private Long id;
    private Long ownerId;
    private String name;
    private String type;
    private double proteins;
    private double fats;
    private double carbohydrates;

    public ProductDto() { }

    public Product toProduct() {
        Product product = new Product();

        product.setId(id);
        product.setName(name);
        try {
            product.setType(ProductType.valueOf(type));
        } catch (Exception wrongTypeName) {
            product.setType(null);
        }

        product.setProteins(proteins);
        product.setFats(fats);
        product.setCarbohydrates(carbohydrates);

        return product;
    }
}
