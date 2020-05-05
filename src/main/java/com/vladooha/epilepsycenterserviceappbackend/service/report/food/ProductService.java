package com.vladooha.epilepsycenterserviceappbackend.service.report.food;

import com.vladooha.epilepsycenterserviceappbackend.model.report.food.Product;
import com.vladooha.epilepsycenterserviceappbackend.model.report.food.ProductType;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductService {
    Product findById(Long id);
    List<Product> getProducts(User owner);
    List<Product> getProducts(User owner, List<ProductType> productTypes, String nameQuery);
    Product merge(Product product);
    Product save(Product product);
}
