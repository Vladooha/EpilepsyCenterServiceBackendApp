package com.vladooha.epilepsycenterserviceappbackend.model.report;


import com.vladooha.epilepsycenterserviceappbackend.model.BaseEntity;
import com.vladooha.epilepsycenterserviceappbackend.model.report.food.Menu;
import com.vladooha.epilepsycenterserviceappbackend.model.report.food.MenuParameters;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "REPORT")
@Getter
@Setter
@EqualsAndHashCode
public class Report extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_ID")
    private User owner;

    @Column(name = "DATE")
    private LocalDate date;

    @Column(name = "KETONES", precision = 5, scale = 2)
    private Double ketones = 0.0;

    @Column(name = "GLUCOSE", precision = 5, scale = 2)
    private Double glucose = 0.0;

    @Column(name = "MESSAGE")
    @Length(max = 1000)
    private String message;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "report")
    private Menu menu;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MENU_PAPRAMETERS")
    private MenuParameters menuParameters;
}
