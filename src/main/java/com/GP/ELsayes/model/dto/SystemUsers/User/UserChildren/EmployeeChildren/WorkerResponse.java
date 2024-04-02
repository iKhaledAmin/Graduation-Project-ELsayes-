package com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren;


import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeResponse;
import com.GP.ELsayes.model.enums.WorkerStatus;
import com.GP.ELsayes.model.enums.roles.WorkerRole;
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
    private WorkerRole workerRole;
    private String score;
    private WorkerStatus workerStatus;
    private Long branchId;
    private Long managerId;
}

