package com.GP.ELsayes.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPackageToOrderListRequest {
    @NotNull(message = "Customer id number must not be null")
    private Long customerId;

    @NotNull(message = "Package id number must not be null")
    private Long packageId;
}
