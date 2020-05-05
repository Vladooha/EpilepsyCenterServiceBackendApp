package com.vladooha.epilepsycenterserviceappbackend.pojo.dto.report;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class ReportDatesDto {
    final List<LocalDate> dateList;

    public ReportDatesDto(List<LocalDate> dateList) {
        this.dateList = dateList;
    }
}
