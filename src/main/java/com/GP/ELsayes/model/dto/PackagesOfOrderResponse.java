package com.GP.ELsayes.model.dto;

import com.GP.ELsayes.model.enums.ProgressStatus;
import com.GP.ELsayes.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferOfOrderResponse {
    private Long id;
    private String name;
    private String actualOfferPrice;
    private String originalTotalRequiredTime;
}
