package com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren;


import com.GP.ELsayes.model.dto.SystemUsers.User.UserResponse;
import com.GP.ELsayes.model.enums.permissions.OwnerPermission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class OwnerResponse extends UserResponse {


    private Long id;
    private String percentage;
    private OwnerPermission ownerPermission;
}
