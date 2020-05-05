package com.vladooha.epilepsycenterserviceappbackend.rest.doctor;

import com.vladooha.epilepsycenterserviceappbackend.facade.doctor.DoctorFacade;
import com.vladooha.epilepsycenterserviceappbackend.model.report.Report;
import com.vladooha.epilepsycenterserviceappbackend.model.report.food.MenuParameters;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import com.vladooha.epilepsycenterserviceappbackend.pojo.dto.report.ReportDto;
import com.vladooha.epilepsycenterserviceappbackend.pojo.dto.report.food.MenuParametersDto;
import com.vladooha.epilepsycenterserviceappbackend.pojo.dto.user.UserDto;
import com.vladooha.epilepsycenterserviceappbackend.rest.AbstractRestController;
import com.vladooha.epilepsycenterserviceappbackend.service.report.ReportService;
import com.vladooha.epilepsycenterserviceappbackend.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
public class DoctorRestController extends AbstractRestController {
    private final DoctorFacade doctorFacade;
    private final ReportService reportService;

    public DoctorRestController(UserService userService, DoctorFacade doctorFacade, ReportService reportService) {
        super(userService);
        this.doctorFacade = doctorFacade;
        this.reportService = reportService;
    }

    @GetMapping("/doctors/patients")
    @Transactional
    public ResponseEntity getPatients(Authentication authentication) {
        User user = getUserByAuth(authentication);
        if (null == user || !userService.isDoctor(user)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        List<User> patients = user.getPatients();
        if (null == patients || patients.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

        List<UserDto> patientDtos = patients.stream()
                .map((patient) -> UserDto.fromUser(patient))
                .collect(Collectors.toList());

        return new ResponseEntity(patientDtos, HttpStatus.OK);
    }

    @PostMapping("/doctors/patients")
    public ResponseEntity postPatients(@RequestBody UserDto patientDto,
                                       Authentication authentication) {
        User user = getUserByAuth(authentication);
        if (null == user || !userService.isDoctor(user)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        User patient = userService.findByClinicId(patientDto.getClinicId());
        if (null == patient) {
            return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        patient = userService.addPatient(user, patient);
        if (null == patient) {
            return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity(UserDto.fromUser(patient), HttpStatus.CREATED);
    }

    @GetMapping("/doctors/patients/{patientId}/menuParameters")
    public ResponseEntity postMenuParameters(@PathVariable(name = "patientId") Long patientId,
                                             Authentication authentication) {
        User user = getUserByAuth(authentication);
        if (null == user || !userService.isDoctor(user)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        User patient = userService.findById(patientId);
        if (null == patient) {
            return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        MenuParameters menuParameters = doctorFacade.getActualMenuParameters(user, patient);
        if (null == menuParameters) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity(MenuParametersDto.fromMenuParameters(menuParameters), HttpStatus.OK);
    }

    @PostMapping("/doctors/patients/{patientId}/menuParameters")
    public ResponseEntity postMenuParameters(@PathVariable(name = "patientId") Long patientId,
                                             @RequestBody MenuParametersDto menuParametersDto,
                                             Authentication authentication) {
        User user = getUserByAuth(authentication);
        if (null == user || !userService.isDoctor(user)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        User patient = userService.findById(patientId);
        if (null == patient) {
            return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        MenuParameters savedMenuParameters = doctorFacade.createNewMenuParameters(
                menuParametersDto.toMenuParameters(),
                user,
                patient
        );
        if (null == savedMenuParameters) {
            return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity(MenuParametersDto.fromMenuParameters(savedMenuParameters), HttpStatus.CREATED);
    }
}
