package com.GP.ELsayes.model.dto.relations;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerVisitationsOfBranchesRequest {
    @NotNull(message = "Customer id must not be null")
    private Long customerId;

    @NotNull(message = "Branch id must not be null")
    private Long branchId;
}
