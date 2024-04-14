package com.GP.ELsayes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private List<ServicesOfOrderResponse> services;
    private List<PackagesOfOrderResponse> packages;
    private String OrderTotalCost;
    private String totalRequiredTime ;
}

