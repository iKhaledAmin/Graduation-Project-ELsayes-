package com.GP.ELsayes.model.dto;

import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BranchResponse {
    private Long id;
    private String name;
    private String location;
    private String capacityOfCars;
    private Long managerId;
    private Long ownerIdCreateIt;
}
