package com.vladooha.epilepsycenterserviceappbackend.repo.report.food;

import com.vladooha.epilepsycenterserviceappbackend.model.report.food.Product;
import com.vladooha.epilepsycenterserviceappbackend.model.report.food.ProductType;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByOwner(User owner);
    List<Product> findAllByOwnerAndTypeInAndNameContainsIgnoreCase(User owner, Iterable<ProductType> productTypes, String nameQuery);
}
