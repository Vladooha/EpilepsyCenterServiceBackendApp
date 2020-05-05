package com.vladooha.epilepsycenterserviceappbackend.pojo.dto.report.food;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@EqualsAndHashCode
public class ProductWeightDto {
    private ProductDto product;
    private Double weight;
}
