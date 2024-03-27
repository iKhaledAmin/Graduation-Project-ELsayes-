package com.GP.ELsayes.model.dto;

import com.GP.ELsayes.model.enums.CarType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCarToCustomerRequest {
    @NotNull(message = "Car plate number must not be null")
    private String carPlateNumber;

    @NotNull(message = "Customer id number must not be null")
    private Long customerId;

    @NotNull(message = "Car type must not be null")
    private CarType carType;

    private Date model;
}
