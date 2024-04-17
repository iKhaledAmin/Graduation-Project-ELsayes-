package com.GP.ELsayes.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPackageRequest {
    @NotNull(message = "Service id number must not be null")
    private Long packageId;


    private Long branchId;
}
