package com.GP.ELsayes.model.dto;

import com.GP.ELsayes.model.enums.ServiceCategory;
import com.GP.ELsayes.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResponse {

    private Long id;
    private String name;
    private String description;
    private String serviceImageURL;
    private String price;
    private String requiredTime;
    private ServiceCategory serviceCategory;
   // private Status statusInBranch;
   private boolean availableInBranch;
}
