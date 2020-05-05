package com.vladooha.epilepsycenterserviceappbackend.facade.user;

import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import com.vladooha.epilepsycenterserviceappbackend.pojo.ErrorContainer;
import com.vladooha.epilepsycenterserviceappbackend.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum UserErrors {
    BAD_CLINIC_ID("ID пациента клиники занят или введён некорректно"),
    BAD_LOGIN("Некорректный логин"),
    BAD_EMAIL("Некорректный e-mail"),
    REGISTERED_EMAIL("E-mail уже зарегистрирован"),
    BAD_PASSWORD("Некорректный пароль"),
    BAD_NAME("Некорректные имя/фамилия");

    private String description;

    UserErrors(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

@Component
public class UserFacade {
    private static final int MAX_EMAIL_LENGTH = 35;
    private static final int MAX_NAME_LENGTH = 25;

    @Autowired
    private UserService userService;

    public ErrorContainer validateUser(User user) {
        ErrorContainer errorContainer = new ErrorContainer();

        errorContainer.concat(validateClinicId(user.getClinicId()));
        errorContainer.concat(validateEmail(user.getEmail()));
        errorContainer.concat(validatePassword(user.getPassword()));
        errorContainer.concat(validateNameAndSurname(user.getName(), user.getSurname()));

        return errorContainer;
    }

    private ErrorContainer validateClinicId(String clinicId) {
        if (null == clinicId || clinicId.length() != 8) {
            return new ErrorContainer(UserErrors.BAD_CLINIC_ID.getDescription());
        }

        User oldUser = userService.findByClinicId(clinicId);
        if (oldUser != null) {
            return new ErrorContainer(UserErrors.BAD_CLINIC_ID.getDescription());
        }

        return new ErrorContainer();
    }

    private ErrorContainer validateLogin(String login) {
        if (login == null) {
            return new ErrorContainer(UserErrors.BAD_LOGIN.getDescription());
        }

        ErrorContainer errorContainer = new ErrorContainer();

        Pattern loginPattern = Pattern.compile("^[A-Za-z0-9]{5,15}$");
        Matcher loginMatcher = loginPattern.matcher(login);
        if (!loginMatcher.matches()) {
            errorContainer.addErrors(UserErrors.BAD_LOGIN.getDescription());
        }

        return errorContainer;
    }

    private ErrorContainer validatePassword(String password) {
        if (password == null) {
            return new ErrorContainer(UserErrors.BAD_PASSWORD.getDescription());
        }

        ErrorContainer errorContainer = new ErrorContainer();

        Pattern notAllowedSymbolsPattern = Pattern.compile("[^A-Za-z\\d!.]+");
        Matcher notAllowedSymbolsMatcher = notAllowedSymbolsPattern.matcher(password);
        if (notAllowedSymbolsMatcher.matches()) {
            errorContainer.addErrors(UserErrors.BAD_PASSWORD.getDescription());
        }

        if (errorContainer.hasErrors()) {
            return errorContainer;
        }

        Pattern symbolsCountPattern = Pattern.compile(".*[A-Z]+.*[0-9]+.*|.*[0-9]+.*[A-Z]+.*$");
        Matcher symbolsCountMatcher = symbolsCountPattern.matcher(password);
        if (!symbolsCountMatcher.matches()) {
            errorContainer.addErrors(UserErrors.BAD_PASSWORD.getDescription());
        }

        return errorContainer;
    }

    private ErrorContainer validateEmail(String email) {
        if (email == null) {
            return new ErrorContainer(UserErrors.BAD_EMAIL.getDescription());
        }

        ErrorContainer errorContainer = new ErrorContainer();

        User oldUser = userService.findByEmail(email);
        if (oldUser != null) {
            errorContainer.addErrors(UserErrors.REGISTERED_EMAIL.getDescription());

            return errorContainer;
        }

        Pattern allowedSymbolsPattern = Pattern.compile("^[^@]+@[^@]+\\.[^@]+$");
        Matcher allowedSymbolsMatcher = allowedSymbolsPattern.matcher(email);
        if (email.length() > MAX_EMAIL_LENGTH || !allowedSymbolsMatcher.matches()) {
            errorContainer.addErrors(UserErrors.BAD_EMAIL.getDescription());
        }

        return errorContainer;
    }

    private ErrorContainer validateNameAndSurname(String name, String surname) {
        if (name == null || surname == null) {
            return new ErrorContainer(UserErrors.BAD_NAME.getDescription());
        }

        ErrorContainer errorContainer = new ErrorContainer();

        if (name.length() > MAX_NAME_LENGTH || surname.length() > MAX_NAME_LENGTH) {
            errorContainer.addErrors(UserErrors.BAD_NAME.getDescription());
        }

        return errorContainer;
    }
}
