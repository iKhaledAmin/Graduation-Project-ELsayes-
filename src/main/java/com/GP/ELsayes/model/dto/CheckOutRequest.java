package com.GP.ELsayes.model.dto;

import com.GP.ELsayes.model.enums.CarType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckOutRequest {

    @NotNull(message = "Car plate number must not be null")
    @NotEmpty(message = "Car plate number must not be empty")
    private String carPlateNumber;


    @NotNull(message = "Branch id must not be null")
    private Long branchId;
}
