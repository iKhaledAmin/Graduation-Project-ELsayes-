package com.GP.ELsayes.model.dto.relations;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicesOfPackageRequest {

    @NotNull(message = "Service id must not be null")
    private Long serviceId;

    @NotNull(message = "Package id must not be null")
    private Long packageId;
}

