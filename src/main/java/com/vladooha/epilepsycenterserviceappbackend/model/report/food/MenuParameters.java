package com.vladooha.epilepsycenterserviceappbackend.model.report.food;

import com.vladooha.epilepsycenterserviceappbackend.model.BaseEntity;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "MENU_PARAMETERS")
@Getter
@Setter
@EqualsAndHashCode
public class MenuParameters extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOCTOR")
    private User doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PATIENT")
    private User patient;

    @Column(name = "DATE")
    private LocalDateTime date;

    @Column(name = "CALORIES")
    @Max(10000)
    @Min(0)
    private Integer calories;

    @Column(name = "PROTEINS", precision = 5, scale = 2)
    @Max(10000)
    @Min(0)
    private Double proteins;

    @Column(name = "RATIO", precision = 4, scale = 2)
    private Double ratio;
}
