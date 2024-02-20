package com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren;

import com.GP.ELsayes.model.dto.SystemUsers.User.UserRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CustomerRequest extends UserRequest {
    private String firstTrialCode;
}
