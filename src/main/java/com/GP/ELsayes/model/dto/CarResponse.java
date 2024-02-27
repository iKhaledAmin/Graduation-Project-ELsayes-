package com.GP.ELsayes.model.dto;

import com.GP.ELsayes.model.enums.CarType;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date model;
    private Long customerId;
}
