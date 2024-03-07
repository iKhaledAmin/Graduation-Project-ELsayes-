package com.GP.ELsayes.model.dto.relations;

import com.GP.ELsayes.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicesOfOffersResponse {

    private String serviceName;
    private String  offerName;
    Date addingDate;
}
