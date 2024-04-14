package com.GP.ELsayes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckOutResponse {
    private String carPlateNumber;
    private String customerName;
    private String parkingPeriod;
    private String parkingPrice;
    private OrderResponse order;
    private String totalCost;
}
