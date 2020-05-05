package com.vladooha.epilepsycenterserviceappbackend.rest;

import com.vladooha.epilepsycenterserviceappbackend.model.report.food.MenuParameters;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import com.vladooha.epilepsycenterserviceappbackend.service.report.food.MenuParametersService;
import com.vladooha.epilepsycenterserviceappbackend.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@CrossOrigin
@RestController
public class TestRestController extends AbstractRestController {
    private final MenuParametersService menuParametersService;

    public TestRestController(UserService userService, MenuParametersService menuParametersService) {
        super(userService);
        this.menuParametersService = menuParametersService;
    }

    @GetMapping("/report_test")
    public ResponseEntity getTest() {
        MenuParameters menuParameters = new MenuParameters();
        User patient = userService.findById(1L);
        User doctor = userService.findById(2L);

        menuParameters.setPatient(patient);
        menuParameters.setDoctor(doctor);
        menuParameters.setDate(LocalDateTime.now().minusDays(2));
        menuParameters.setProteins(2.0);
        menuParameters.setCalories(2000);
        menuParameters.setRatio(1.3);

        menuParametersService.save(menuParameters);

        return new ResponseEntity(HttpStatus.OK);
    }
}
