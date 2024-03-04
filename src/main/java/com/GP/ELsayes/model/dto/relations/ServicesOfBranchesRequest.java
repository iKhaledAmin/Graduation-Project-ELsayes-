package com.GP.ELsayes.model.dto.relations;

import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicesOfBranchesRequest {

    @NotNull(message = "Service id must not be null")
    private Long serviceId;

    @NotNull(message = "Branch id must not be null")
    private Long branchId;
}
