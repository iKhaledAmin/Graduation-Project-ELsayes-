package com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren;


import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeResponse;
import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.enums.roles.WorkerRole;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class WorkerResponse  extends EmployeeResponse {
    private Long id;
    private String Score;
    WorkerRole workerRole;

    //@JsonBackReference
    //@JsonIgnore
    @JsonManagedReference
    Branch branchWorkOn;
}

