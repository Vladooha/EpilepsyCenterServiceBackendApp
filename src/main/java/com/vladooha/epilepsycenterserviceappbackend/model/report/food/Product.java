package com.vladooha.epilepsycenterserviceappbackend.model.report.food;

import com.vladooha.epilepsycenterserviceappbackend.model.BaseEntity;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
@Table(name = "PRODUCT")
@Data
public class Product extends BaseEntity {
    @Column(name = "NAME", nullable = false)
    @Length(max = 35)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_ID")
    private User owner;

    @Column(name = "PROTEINS", precision = 8, scale = 2)
    private Double proteins;

    @Column(name = "FATS", precision = 8, scale = 2)
    private Double fats;

    @Column(name = "CARBONHYDRATES", precision = 8, scale = 2)
    private Double carbonhydrates;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "products")
    private List<Dish> dishes;
}
