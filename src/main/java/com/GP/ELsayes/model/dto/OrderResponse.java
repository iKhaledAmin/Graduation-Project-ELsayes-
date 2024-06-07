package com.GP.ELsayes.model.dto;

import com.GP.ELsayes.model.enums.ProgressStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long id;
    private Date orderDate;
    private Date orderFinishDate;
    private String totalRequiredTime;
    private String totalPrice;
    private ProgressStatus progressStatus;

    private List<ServicesOfOrderResponse> services;
    private List<PackagesOfOrderResponse> packages;
    private String OrderTotalCost;

    private String customerName;


}

