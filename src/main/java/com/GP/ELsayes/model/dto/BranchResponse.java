package com.GP.ELsayes.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchResponse {
    private Long id;
    private String name;
    private String location;
    private String capacityOfCars;
    private Long managerId;

    private String profitOfDay;
    private String profitOfMonth;
    private String profitOfYear;
    private String totalProfit;
}
