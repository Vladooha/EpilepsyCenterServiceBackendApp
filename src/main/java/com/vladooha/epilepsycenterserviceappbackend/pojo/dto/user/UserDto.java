package com.vladooha.epilepsycenterserviceappbackend.pojo.dto.user;

import com.vladooha.epilepsycenterserviceappbackend.model.user.User;
import com.vladooha.epilepsycenterserviceappbackend.model.user.UserRole;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserDto {
    public enum UserRoleDto {
        PATIENT(UserRole.PATIENT),
        DOCTOR(UserRole.DOCTOR),
        ADMIN(UserRole.ADMIN);

        private final String roleName;

        UserRoleDto(String roleName) {
            this.roleName = roleName;
        }

        public String getRoleName() {
            return roleName;
        }
    }

    public static UserDto fromUser(User user) {
        return new UserDto(
                user.getId(),
                user.getClinicId(),
                user.getEmail(),
                user.getUserRoles(),
                user.getName(),
                user.getSurname(),
                user.getPhone()
        );
    }

    public static UserDto fromUserWithoutPrivate(User user) {
        return new UserDto(
                user.getId(),
                user.getClinicId(),
                user.getUserRoles(),
                user.getName(),
                user.getSurname()
        );
    }

    private Long id;
    private String clinicId;
    private String email;
    private String password;
    private List<UserRoleDto> userRoles;
    private String name;
    private String surname;
    private String phone;

    public UserDto() {}

    public UserDto(
            Long id,
            String clinicId,
            List<UserRole> userRoles,
            String name,
            String surname) {
        this.id = id;
        this.clinicId = clinicId;
        this.userRoles = mapUserRolesToDto(userRoles);
        this.name = name;
        this.surname = surname;
    }

    public UserDto(
            Long id,
            String clinicId,
            String email,
            List<UserRole> userRoles,
            String name,
            String surname,
            String phone) {
        this.id = id;
        this.clinicId = clinicId;
        this.email = email;
        this.userRoles = mapUserRolesToDto(userRoles);
        this.name = name;
        this.surname = surname;
        this.phone = phone;
    }

    private static List<UserRoleDto> mapUserRolesToDto(List<UserRole> userRoles) {
        if (userRoles != null) {
            return new ArrayList<>(userRoles)
                    .stream()
                    .map((userRole -> {
                        String name = userRole.getName();

                        if (UserRole.ADMIN.equals(name)) {
                            return UserRoleDto.ADMIN;
                        } else if (UserRole.DOCTOR.equals(name)) {
                            return UserRoleDto.DOCTOR;
                        } else if (UserRole.PATIENT.equals(name)) {
                            return UserRoleDto.PATIENT;
                        }

                        return null;
                    }))
                    .filter(userRoleDto -> userRoleDto != null)
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }
}
