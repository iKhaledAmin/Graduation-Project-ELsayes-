package com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren;


import com.GP.ELsayes.model.dto.SystemUsers.User.UserRequest;
import com.GP.ELsayes.model.enums.permissions.OwnerPermission;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.lang.String;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class OwnerRequest extends UserRequest {

//    @NotNull(message = "Owner permission must not be null")
//    private OwnerPermission ownerPermission;

    private Long oldOwnerId;

}
