package com.GP.ELsayes.model.dto.relations;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OffersOfBranchesRequest {
    @NotNull(message = "Offer id must not be null")
    private Long offerId;

    @NotNull(message = "Branch id must not be null")
    private Long branchId;
}
