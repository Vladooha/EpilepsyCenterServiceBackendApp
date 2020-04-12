package com.vladooha.epilepsycenterserviceappbackend.model.user;

import com.vladooha.epilepsycenterserviceappbackend.model.BaseEntity;
import com.vladooha.epilepsycenterserviceappbackend.model.report.food.Product;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
@Table(name = "USER")
@Data
public class User extends BaseEntity {
    @Column(name = "EMAIL", unique = true, nullable = false)
    @Pattern(regexp = "^[^@]+@[^@]+\\.[^@]+$")
    @Length(max = 35)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    @Length(min = 8)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "users")
    private List<UserRole> userRoles;

    @Column(name = "NAME", nullable = false)
    @Length(min = 2, max = 20)
    private String name;

    @Column(name = "SURNAME", nullable = false)
    @Length(min = 2, max = 20)
    private String surname;

    @Column(name = "PHONE", unique = true)
    @Pattern(regexp = "^(?:[+0-9][0-9]{11})?$")
    private String phone;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Product> products;
}
