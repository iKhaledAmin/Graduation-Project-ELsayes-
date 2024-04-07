package com.GP.ELsayes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsResponse {

    private List<ServiceOfOrderResponse> services;
    private List<OfferOfOrderResponse> offers;

}
