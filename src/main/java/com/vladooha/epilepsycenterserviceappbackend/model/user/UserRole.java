package com.vladooha.epilepsycenterserviceappbackend.model.user;

import com.vladooha.epilepsycenterserviceappbackend.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "USER_ROLE")
@Getter
@Setter
@EqualsAndHashCode
public class UserRole extends BaseEntity {
    final public static String PATIENT = "patient";
    final public static String DOCTOR = "doctor";
    final public static String ADMIN = "admin";

    @Column(name = "NAME", unique = true)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")})
    private List<User> users;
}
