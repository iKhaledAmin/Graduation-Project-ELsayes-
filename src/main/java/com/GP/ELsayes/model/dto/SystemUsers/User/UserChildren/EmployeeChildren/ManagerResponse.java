package com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren;

import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeResponse;
import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.enums.permissions.ManagerPermission;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ManagerResponse extends EmployeeResponse {

    private Long id;
    private ManagerPermission managerPermission;
    //@JsonIgnore
    private Branch branch;

}
