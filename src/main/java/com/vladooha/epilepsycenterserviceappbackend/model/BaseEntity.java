package com.vladooha.epilepsycenterserviceappbackend.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Data
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CREATED")
    private Date created;

    @Column(name = "UPDATED")
    private Date updated;
}
