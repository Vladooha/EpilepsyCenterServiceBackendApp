package com.vladooha.epilepsycenterserviceappbackend.rest.user;

import com.vladooha.epilepsycenterserviceappbackend.facade.user.UserFacade;
import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import com.vladooha.epilepsycenterserviceappbackend.model.user.UserRole;
import com.vladooha.epilepsycenterserviceappbackend.pojo.ErrorContainer;
import com.vladooha.epilepsycenterserviceappbackend.pojo.dto.user.UserDto;
import com.vladooha.epilepsycenterserviceappbackend.security.jwt.JwtTokenProvider;
import com.vladooha.epilepsycenterserviceappbackend.service.user.UserRoleService;
import com.vladooha.epilepsycenterserviceappbackend.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserRestController {
    final static String API_ROOT = "/api";

    private final UserService userService;
    private final UserRoleService userRoleService;

    private final UserFacade userFacade;

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserRestController(UserService userService, UserRoleService userRoleService, UserFacade userFacade, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.userFacade = userFacade;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping(value = API_ROOT + "/user/sign-up")
    public ResponseEntity signUp(@RequestBody UserDto userDto, HttpServletResponse response) {
        User user = parseUserDto(userDto);

        ErrorContainer errorContainer = userFacade.validateUser(user);

        if (!errorContainer.hasErrors()) {
            userService.signUp(user);

            return logIn(userDto, response);
        } else {
            return new ResponseEntity(errorContainer, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping(value = API_ROOT + "/user/log-in")
    public ResponseEntity logIn(@RequestBody UserDto userDto, HttpServletResponse response) {
        User user = parseUserDto(userDto);
        user = userService.logIn(user);

        if (user == null) {
            ErrorContainer errorContainer = new ErrorContainer("Wrong login data!");
            return new ResponseEntity(errorContainer, HttpStatus.FORBIDDEN);
        }

        jwtTokenProvider.createResponseTokens(user, response);

        userDto = UserDto.fromUser(user);

        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @GetMapping(value = API_ROOT + "/user/refresh-token")
    public ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String userEmail = jwtTokenProvider.parseAndRefreshResponseTokens(request, response);

        if (userEmail != null) {
            User user = userService.findByEmail(userEmail);
            UserDto userDto = UserDto.fromUser(user);

            return new ResponseEntity(userDto, HttpStatus.OK);
        }

        ErrorContainer errorContainer = new ErrorContainer("Wrong refresh token!");
        return new ResponseEntity(errorContainer, HttpStatus.UNAUTHORIZED);
    }

    @GetMapping(value = API_ROOT + "/user")
    public ResponseEntity getUser(Authentication authentication) {
        String email = authentication.getName();

        User user = userService.findByEmail(email);
        if (user != null) {
            UserDto userDto = UserDto.fromUser(user);

            return new ResponseEntity(userDto, HttpStatus.OK);
        }

        ErrorContainer errorContainer = new ErrorContainer("Wrong login data!");
        return new ResponseEntity(errorContainer, HttpStatus.FORBIDDEN);
    }

    @GetMapping(value = API_ROOT + "/user/check-status")
    public ResponseEntity<String> checkStatus() {
        return new ResponseEntity<>("Authorized!", HttpStatus.OK);
    }

    private User parseUserDto(UserDto userDto) {
        User user = new User();

        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setUserRoles(mapUserRolesDto(userDto.getUserRoles()));

        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setPhone(userDto.getPhone());

        return user;
    }

    private List<UserRole> mapUserRolesDto(List<UserDto.UserRoleDto> userRoleDtos) {
        if (userRoleDtos != null) {
            return new ArrayList<>(userRoleDtos)
                    .stream()
                    .map(userRoleDto -> userRoleService.findByName(userRoleDto.getRoleName()))
                    .filter(userRole -> userRole != null)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }
}
