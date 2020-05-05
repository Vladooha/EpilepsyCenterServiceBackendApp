package com.vladooha.epilepsycenterserviceappbackend.rest.report;

import com.vladooha.epilepsycenterserviceappbackend.facade.report.ReportFacade;
import com.vladooha.epilepsycenterserviceappbackend.model.report.Report;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import com.vladooha.epilepsycenterserviceappbackend.pojo.dto.report.ReportDatesDto;
import com.vladooha.epilepsycenterserviceappbackend.pojo.dto.report.ReportDto;
import com.vladooha.epilepsycenterserviceappbackend.rest.AbstractRestController;
import com.vladooha.epilepsycenterserviceappbackend.service.report.ReportService;
import com.vladooha.epilepsycenterserviceappbackend.service.user.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
public class ReportRestController extends AbstractRestController {
    private final ReportService reportService;
    private final ReportFacade reportFacade;

    public ReportRestController(UserService userService, ReportService reportService, ReportFacade reportFacade) {
        super(userService);
        this.reportService = reportService;
        this.reportFacade = reportFacade;
    }

    @GetMapping("/reports/dates")
    public ResponseEntity get(Authentication authentication) {
        User user = getUserByAuth(authentication);
        if (null == user) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        List<LocalDate> dateList = reportService.findAllCreatedOrCreatableReportDates(user);
        if (dateList == null) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity(new ReportDatesDto(dateList), HttpStatus.OK);
    }

    @GetMapping(value = "/reports/dates", params = {"userId"})
    public ResponseEntity get(@RequestParam(name = "userId") Long userId,
                              Authentication authentication) {
        User user = getUserByAuth(authentication);
        if (null == user) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        User patient = userService.findById(userId);
        if (null == patient || (!userService.isDoctorsPatient(user, patient) && user.getId() != userId)) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

        List<LocalDate> dateList = reportService.findAllCreatedOrCreatableReportDates(patient);
        if (dateList == null) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity(new ReportDatesDto(dateList), HttpStatus.OK);
    }

    @GetMapping("/reports")
    public ResponseEntity get(@RequestParam(name = "date") String dateStr,
                              Authentication authentication) {
        User user = getUserByAuth(authentication);
        if (null == user) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        return getReport(user, dateStr, true);
    }

    @GetMapping(value = "/reports", params = {"userId"})
    public ResponseEntity get(@RequestParam(name = "userId") Long userId,
                              @RequestParam(name = "date") String dateStr,
                              Authentication authentication) {
        User user = getUserByAuth(authentication);
        if (null == user) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        User patient = userService.findById(userId);
        if (null == patient || (!userService.isDoctorsPatient(user, patient) && user.getId() != userId)) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

        return getReport(patient, dateStr, false);
    }

    @PostMapping(value = "/reports", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity post(@RequestBody ReportDto reportDto, Authentication authentication) {
        User user = getUserByAuth(authentication);
        if (null == user) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        LocalDate reportDate = reportDto.getDate();
        if (null == reportDate || isReportAlreadyExists(user, reportDate)) {
            return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Report report = saveNewReport(reportDto, user);
        if (null == report) {
            return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity(ReportDto.fromReport(report), HttpStatus.CREATED);
    }

    private ResponseEntity getReport(User user, String dateStr, boolean createIfAbsent) {
        LocalDate date = LocalDate.parse(dateStr);
        if (null == date) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

        Report report = reportService.findByOwnerAndDate(user, date);
        if (null == report) {
            if (createIfAbsent) {
                ReportDto newReportDto = createEmptyReport(user, date);
                if (newReportDto != null) {
                    return new ResponseEntity(newReportDto, HttpStatus.OK);
                } else {
                    return new ResponseEntity(HttpStatus.NO_CONTENT);
                }
            } else {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
        }

        return new ResponseEntity(ReportDto.fromReport(report), HttpStatus.OK);
    }

    private boolean isReportAlreadyExists(User owner, LocalDate date) {
        Report existingReport = reportService.findByOwnerAndDate(owner, date);

        return existingReport != null;
    }

    private ReportDto createEmptyReport(User patient, LocalDate reportDate) {
        Report emptyReport = reportService.createEmpty(patient, reportDate);
        if (emptyReport != null) {
            ReportDto reportDto = ReportDto.fromEmptyReport(emptyReport);

            return reportDto;
        }

        return null;
    }

    private Report saveNewReport(ReportDto reportDto, User patient) {
        Report report = reportDto.toUserReport();
        report.setOwner(patient);

        try {
            Report postedReport = reportFacade.postReport(report, patient);

            return postedReport;
        } catch (DataIntegrityViolationException e) {
            return null;
        }
    }
}
