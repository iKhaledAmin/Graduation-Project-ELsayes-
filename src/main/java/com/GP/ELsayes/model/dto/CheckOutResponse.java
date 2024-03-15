package com.GP.ELsayes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckOutResponse {
    private String customerName;
    private String carPlateNumber;
    private String periodParking;
    private String totalCost;
}
