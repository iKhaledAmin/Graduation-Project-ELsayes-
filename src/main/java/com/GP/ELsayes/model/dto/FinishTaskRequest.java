package com.GP.ELsayes.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinishTaskRequest {
    @NotNull(message = "Car plate number must not be null")
    private String carPlateNumber;


    @NotNull(message = "Worker id must not be null")
    private Long workerId;
}
