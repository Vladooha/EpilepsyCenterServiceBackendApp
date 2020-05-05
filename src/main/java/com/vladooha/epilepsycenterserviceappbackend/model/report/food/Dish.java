package com.vladooha.epilepsycenterserviceappbackend.model.report.food;

import com.vladooha.epilepsycenterserviceappbackend.model.BaseEntity;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "DISH")
@Getter
@Setter
@EqualsAndHashCode
public class Dish extends BaseEntity {
    @Column(name = "NAME", nullable = false)
    @Length(max = 35)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_ID")
    private User owner;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "DISH_PRODUCTS",
            joinColumns = @JoinColumn(name = "DISH_ID"),
            inverseJoinColumns = @JoinColumn(name = "PRODCUT_ID"))
    private List<Product> productList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dish")
    private List<DishParameters> dishParametersList;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "LAST_PARAMETERS_ID", referencedColumnName = "ID")
    private DishParameters lastDishParameters;
}
