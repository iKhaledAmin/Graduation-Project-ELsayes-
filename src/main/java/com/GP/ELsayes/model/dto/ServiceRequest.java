package com.GP.ELsayes.model.dto;

import com.GP.ELsayes.model.enums.ServiceCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ServiceRequest {

    private String name;
    private String description;
    private String serviceImageURL;
    private String price;
    private String requiredTime;
    private ServiceCategory serviceCategory;
}
