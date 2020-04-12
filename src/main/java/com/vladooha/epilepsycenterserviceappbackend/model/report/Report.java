package com.vladooha.epilepsycenterserviceappbackend.model.report;


import com.vladooha.epilepsycenterserviceappbackend.model.BaseEntity;
import com.vladooha.epilepsycenterserviceappbackend.model.report.food.Menu;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "REPORT")
@Data
public class Report extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OWNER_ID")
    private User owner;

    @Column(name = "DATE")
    private Date date;

    @Column(name = "KETONES", precision = 5, scale = 2)
    private Double ketones;

    @Column(name = "GLUCOSE", precision = 5, scale = 2)
    private Double glucose;

    @Column(name = "MESSAGE")
    @Length(max = 1000)
    private String message;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "report")
    private Menu menu;
}
