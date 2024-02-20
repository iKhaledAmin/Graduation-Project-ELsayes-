package com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren;

import com.GP.ELsayes.model.dto.SystemUsers.User.UserResponse;
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
    private Date dateOfJoining;
    private String firstTrialCode;
}
