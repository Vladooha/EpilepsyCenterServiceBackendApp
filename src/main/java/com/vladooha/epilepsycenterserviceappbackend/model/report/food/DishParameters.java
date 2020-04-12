package com.vladooha.epilepsycenterserviceappbackend.model.report.food;

import com.vladooha.epilepsycenterserviceappbackend.model.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "DISH_PARAMETERS")
@Data
public class DishParameters extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DISH_ID", nullable = false)
    private Dish dish;

    @Column(name = "PERCENTAGE")
    private Double percentage;

    @Column(name = "EATING")
    @Enumerated(EnumType.STRING)
    private Eating eating;

    @ElementCollection
    @CollectionTable(name = "PRODUCT_WEIGHT")
    @MapKeyJoinColumn(name = "PRODUCT_ID")
    @Column(name = "WEIGHT", precision = 8, scale = 2)
    private Map<Product, Double> productWeightMap;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "MENU_ID", referencedColumnName = "ID")
    private Menu menu;
}
