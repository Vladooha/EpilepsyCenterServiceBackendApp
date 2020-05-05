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
@Table(name = "PRODUCT")
@Getter
@Setter
@EqualsAndHashCode
public class Product extends BaseEntity {
    @Column(name = "NAME", nullable = false)
    @Length(max = 35)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "OWNER_ID")
    private User owner;

    @Column(name = "TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Column(name = "PROTEINS", precision = 8, scale = 2, nullable = false)
    private Double proteins;

    @Column(name = "FATS", precision = 8, scale = 2, nullable = false)
    private Double fats;

    @Column(name = "CARBOHYDRATES", precision = 8, scale = 2, nullable = false)
    private Double carbohydrates;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "productList")
    private List<Dish> dishes;
}
