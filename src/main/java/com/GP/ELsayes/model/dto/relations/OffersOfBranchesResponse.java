package com.GP.ELsayes.model.dto.relations;

import com.GP.ELsayes.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OffersOfBranchesResponse {
    private String offerName;
    private String  branchName;
    Status offerStatus;
    Date addingDate;
}
