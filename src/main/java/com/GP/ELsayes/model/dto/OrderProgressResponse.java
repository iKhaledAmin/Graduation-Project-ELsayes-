package com.GP.ELsayes.model.dto;

import com.GP.ELsayes.model.enums.ProgressStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderProgressResponse {
    private List<ServicesOfOrderResponse> services;
    private ProgressStatus status;
}
