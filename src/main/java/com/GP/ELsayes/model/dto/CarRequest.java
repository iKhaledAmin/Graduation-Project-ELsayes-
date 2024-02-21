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
public class CarRequest {


    @NotNull(message = "Car plate number must not be null")
    @NotEmpty(message = "Car plate number must not be empty")
    private String carPlateNumber;


    @NotNull(message = "Car type must not be null")
    private CarType carType;


    @NotNull(message = "Cat model must not be null")
    private Date model;


    //@NotNull(message = "Customer id must not be null")
    private Long customerId;

}
