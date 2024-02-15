package com.GP.ELsayes.model.dto;

import com.GP.ELsayes.model.enums.ServiceCategory;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ServiceRequest {

    @NotNull(message = "Name must not be null")
    @NotEmpty(message = "Name must not be empty")
    private String name;

    @NotNull(message = "Description must not be null")
    @NotEmpty(message = "Description must not be empty")
    private String description;

    //@NotNull(message = "Image must not be null")
   // @NotEmpty(message = "Image must not be empty")
    private String serviceImageURL;

    @NotNull(message = "Price must not be null")
    @NotEmpty(message = "Price must not be empty")
    private String price;

    @NotNull(message = "Required tme must not be null")
    @NotEmpty(message = "Required time not be empty")
    private String requiredTime;

    //@NotNull(message = "ServiceEntity category must not be null")
    //@NotEmpty(message = "ServiceEntity category must not be empty")
    private ServiceCategory serviceCategory;
}
