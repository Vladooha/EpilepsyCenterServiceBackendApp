package com.vladooha.epilepsycenterserviceappbackend.model.report.food;

import com.vladooha.epilepsycenterserviceappbackend.model.BaseEntity;
import com.vladooha.epilepsycenterserviceappbackend.model.report.Report;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "MENU")
@Data
public class Menu extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_ID")
    private User owner;

    @Column(name = "DATE")
    private Date date;

    @Column(name = "CALORIES")
    @Max(10000)
    @Min(0)
    private Integer calories;

    @Column(name = "PROTEINS")
    @Max(10000)
    @Min(0)
    private Integer proteins;

    @Column(name = "RATIO", precision = 4, scale = 2)
    private Double ratio;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu")
    private List<DishParameters> dishParametersList;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "REPORT_ID", referencedColumnName = "ID")
    private Report report;
}
