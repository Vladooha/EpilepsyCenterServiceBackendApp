package com.vladooha.epilepsycenterserviceappbackend.pojo.dto.report;

import com.vladooha.epilepsycenterserviceappbackend.model.report.Report;
import com.vladooha.epilepsycenterserviceappbackend.model.report.food.Menu;
import com.vladooha.epilepsycenterserviceappbackend.model.report.food.MenuParameters;
import com.vladooha.epilepsycenterserviceappbackend.pojo.dto.report.food.MenuDto;
import com.vladooha.epilepsycenterserviceappbackend.pojo.dto.report.food.MenuParametersDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
public class ReportDto {
    public static ReportDto fromReport(Report report) {
        ReportDto reportDto = new ReportDto();
        reportDto.setDate(report.getDate());
        reportDto.setKetones(report.getKetones());
        reportDto.setGlucose(report.getGlucose());
        reportDto.setMessage(report.getMessage());

        Menu menu = report.getMenu();
        if (menu != null) {
            reportDto.setMenu(MenuDto.fromMenu(menu));
        }

        MenuParameters menuParameters = report.getMenuParameters();
        if (menuParameters != null) {
            reportDto.setMenuParameters(MenuParametersDto.fromMenuParameters(menuParameters));
        }

        reportDto.setEmpty(false);

        return reportDto;
    }

    public static ReportDto fromEmptyReport(Report report) {
        ReportDto reportDto = fromReport(report);
        reportDto.setEmpty(true);

        return reportDto;
    }

    boolean empty = true;

    LocalDate date;

    double ketones;
    double glucose;

    String message;
    MenuDto menu;
    MenuParametersDto menuParameters;

    public ReportDto() {}

    public Report toUserReport() {
        Report report = new Report();
        report.setDate(date);
        report.setKetones(ketones);
        report.setGlucose(glucose);
        report.setMessage(message);
        if (menu != null) {
            report.setMenu(menu.toMenu());
        }
        if (menuParameters != null) {
            report.setMenuParameters(menuParameters.toMenuParameters());
        }

        return report;
    }
}
