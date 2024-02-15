package com.GP.ELsayes.model.dto;

import com.GP.ELsayes.model.enums.ServiceCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ServiceResponse {

    private Long id;
    private String name;
    private String description;
    private String serviceImageURL;
    private double price;
    private float requiredTime;
    private ServiceCategory serviceCategory;

}
