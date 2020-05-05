package com.vladooha.epilepsycenterserviceappbackend.rest.report.food;

import com.vladooha.epilepsycenterserviceappbackend.model.report.food.MenuParameters;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import com.vladooha.epilepsycenterserviceappbackend.pojo.dto.report.food.MenuParametersDto;
import com.vladooha.epilepsycenterserviceappbackend.rest.AbstractRestController;
import com.vladooha.epilepsycenterserviceappbackend.service.report.food.MenuParametersService;
import com.vladooha.epilepsycenterserviceappbackend.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController("/menus")
public class MenuRestController extends AbstractRestController {
    private final MenuParametersService menuParametersService;

    public MenuRestController(UserService userService, MenuParametersService menuParametersService) {
        super(userService);
        this.menuParametersService = menuParametersService;
    }

    @GetMapping("/parameters")
    public ResponseEntity getParameters(Authentication authentication) {
        User user = getUserByAuth(authentication);

        if (null == user) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        MenuParameters menuParameters = menuParametersService.findActualForPatient(user);
        if (null == menuParameters) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

        MenuParametersDto menuParametersDto = MenuParametersDto.fromMenuParameters(menuParameters);
        return new ResponseEntity(menuParametersDto, HttpStatus.OK);
    }
}
