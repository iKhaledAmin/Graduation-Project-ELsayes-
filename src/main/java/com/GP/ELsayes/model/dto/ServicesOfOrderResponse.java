package com.GP.ELsayes.model.dto;

import com.GP.ELsayes.model.enums.ProgressStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicesOfOrderResponse {
    private Long id;
    private String serviceName;
    private String servicePrice;
    private String requiredTime;
    private ProgressStatus progressStatus;

}
