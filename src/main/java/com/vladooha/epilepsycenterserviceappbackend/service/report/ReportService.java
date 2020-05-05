package com.vladooha.epilepsycenterserviceappbackend.service.report;

import com.vladooha.epilepsycenterserviceappbackend.model.report.Report;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ReportService {
    Report findById(Long id);
    Report findByOwnerAndDate(User owner, LocalDate date);
    List<LocalDate> findAllCreatedReportDates(User owner);
    List<LocalDate> findAllCreatableReportDates(User owner);
    List<LocalDate> findAllCreatedOrCreatableReportDates(User owner);
    Report createEmpty(User patient, LocalDate reportDate);
    boolean isReportCreatable(User owner, LocalDate reportDate);
    Report merge(Report report);
    Report save(Report report);
}
