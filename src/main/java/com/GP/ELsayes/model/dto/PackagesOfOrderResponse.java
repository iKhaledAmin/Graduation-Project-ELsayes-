package com.GP.ELsayes.model.dto;

import com.GP.ELsayes.model.enums.ProgressStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackagesOfOrderResponse {

    private Long id;
    private String packageName;
    private String packagePrice;
    private String requiredTime;
    private ProgressStatus progressStatus;
    private Boolean availableInBranch;
    private String image;
}
