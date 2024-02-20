package com.GP.ELsayes.model.dto;

import com.GP.ELsayes.model.enums.CarType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarResponse {

    private Long id;
    private String carPlateNumber;
    private CarType carType;
    private Date model;
    private Long customerId;
}