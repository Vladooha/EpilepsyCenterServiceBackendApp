package com.vladooha.epilepsycenterserviceappbackend.pojo.dto.report.food;

import com.vladooha.epilepsycenterserviceappbackend.model.report.food.MenuParameters;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class MenuParametersDto {
    public static MenuParametersDto fromMenuParameters(MenuParameters menuParameters) {
        MenuParametersDto menuParametersDto = new MenuParametersDto();

        menuParametersDto.setId(menuParameters.getId());

        menuParametersDto.setCalories(menuParameters.getCalories());
        menuParametersDto.setProteins(menuParameters.getProteins());
        menuParametersDto.setRatio(menuParameters.getRatio());
        menuParametersDto.setDoctorId(menuParameters.getDoctor().getId());
        menuParametersDto.setPatientId(menuParameters.getPatient().getId());

        return menuParametersDto;
    }

    private Long id;
    private long doctorId;
    private long patientId;

    private int calories;
    private double proteins;
    private double ratio;

    public MenuParameters toMenuParameters() {
        MenuParameters menuParameters = new MenuParameters();

        menuParameters.setId(id);

        menuParameters.setCalories(calories);
        menuParameters.setProteins(proteins);
        menuParameters.setRatio(ratio);

        return menuParameters;
    }
}
