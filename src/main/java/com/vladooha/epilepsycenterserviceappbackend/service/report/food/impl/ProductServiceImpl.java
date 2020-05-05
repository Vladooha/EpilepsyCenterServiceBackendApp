package com.vladooha.epilepsycenterserviceappbackend.service.report.food.impl;

import com.vladooha.epilepsycenterserviceappbackend.model.report.food.Product;
import com.vladooha.epilepsycenterserviceappbackend.model.report.food.ProductType;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import com.vladooha.epilepsycenterserviceappbackend.repo.report.food.ProductRepository;
import com.vladooha.epilepsycenterserviceappbackend.service.report.food.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.getOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProducts(User owner) {
        return productRepository.findAllByOwner(owner);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProducts(User owner, List<ProductType> productTypes, String nameQuery) {
        if (null == productTypes || productTypes.isEmpty()) {
            return getProducts(owner);
        }

        if (null == nameQuery) {
            nameQuery = "";
        }

        return productRepository.findAllByOwnerAndTypeInAndNameContainsIgnoreCase(owner, productTypes, nameQuery);
    }

    @Override
    @Transactional
    public Product merge(Product product) {
        Long id = product.getId();
        if (null == id) {
            return save(product);
        }

        Product oldProduct = findById(id);
        if (null == oldProduct) {
            product.setId(null);

            return save(product);
        }

        if (oldProduct.getOwner().getId() != product.getOwner().getId()) {
            return null;
        }

        oldProduct.setName(product.getName());
        oldProduct.setProteins(product.getProteins());
        oldProduct.setFats(product.getFats());
        oldProduct.setCarbohydrates(product.getFats());
        oldProduct.setType(product.getType());

        return save(oldProduct);
    }

    @Override
    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }
}
