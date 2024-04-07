package com.GP.ELsayes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackagesOfOrderResponse {
    private Long id;
    private String name;
    private String actualPackagePrice;
    private String originalTotalRequiredTime;
}
