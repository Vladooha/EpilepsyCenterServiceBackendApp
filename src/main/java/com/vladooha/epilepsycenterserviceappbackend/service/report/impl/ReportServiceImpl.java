package com.vladooha.epilepsycenterserviceappbackend.service.report.impl;

import com.vladooha.epilepsycenterserviceappbackend.model.report.Report;
import com.vladooha.epilepsycenterserviceappbackend.model.report.food.MenuParameters;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import com.vladooha.epilepsycenterserviceappbackend.repo.report.ReportRepository;
import com.vladooha.epilepsycenterserviceappbackend.repo.report.food.MenuParametersRepository;
import com.vladooha.epilepsycenterserviceappbackend.service.report.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    private ReportRepository reportRepository;
    private MenuParametersRepository menuParametersRepository;

    public ReportServiceImpl(ReportRepository reportRepository, MenuParametersRepository menuParametersRepository) {
        this.reportRepository = reportRepository;
        this.menuParametersRepository = menuParametersRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Report findById(Long id) {
        return reportRepository.getOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Report findByOwnerAndDate(User owner, LocalDate date) {
        return reportRepository.findByOwnerAndDate(owner, date);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocalDate> findAllCreatedReportDates(User owner) {
        return reportRepository.findAllDatesByOwner(owner);
    }

    @Override
    public List<LocalDate> findAllCreatableReportDates(User owner) {
        List<LocalDate> creatableReportDates = new ArrayList<>();
        creatableReportDates.add(LocalDate.now());

        return creatableReportDates;
    }

    @Override
    public List<LocalDate> findAllCreatedOrCreatableReportDates(User owner) {
        Set<LocalDate> createdOrCreatableReportDates = new HashSet<>();

        createdOrCreatableReportDates.addAll(findAllCreatedReportDates(owner));
        createdOrCreatableReportDates.addAll(findAllCreatableReportDates(owner));

        return createdOrCreatableReportDates
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public boolean isReportCreatable(User owner, LocalDate reportDate) {
        return findAllCreatableReportDates(owner).contains(reportDate);
    }

    @Override
    @Transactional(readOnly = true)
    public Report createEmpty(User patient, LocalDate reportDate) {
        if (isReportCreatable(patient, reportDate)) {
            MenuParameters menuParameters = menuParametersRepository.findFirstByPatientOrderByDateDesc(patient);
            if (menuParameters != null) {
                Report report = new Report();
                report.setMenuParameters(menuParameters);
                report.setDate(reportDate);

                return report;
            }
        }

        return null;
    }

    @Override
    @Transactional
    public Report merge(Report report) {
        Long id = report.getId();
        if (null == id) {
            return save(report);
        }

        Report oldReport = findById(id);
        if (null == oldReport) {
            report.setId(null);

            return save(report);
        }

        if (oldReport.getOwner().getId() != report.getOwner().getId()) {
            return null;
        }

        oldReport.setMenuParameters(report.getMenuParameters());
        oldReport.setMenu(report.getMenu());
        oldReport.setMessage(report.getMessage());
        oldReport.setGlucose(report.getGlucose());
        oldReport.setKetones(report.getKetones());
        oldReport.setDate(report.getDate());

        return save(oldReport);
    }

    @Override
    @Transactional
    public Report save(Report report) {
        return reportRepository.save(report);
    }
}
