package com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren;

import com.GP.ELsayes.model.dto.SystemUsers.User.UserResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CustomerResponse extends UserResponse {
    private Long id;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateOfJoining;
    private String freeTrialCode;
    private Long currentBranchId;
}
