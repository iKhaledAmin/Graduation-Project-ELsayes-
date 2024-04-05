package com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren;

import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeRequest;
import com.GP.ELsayes.model.enums.WorkerStatus;
import com.GP.ELsayes.model.enums.roles.WorkerRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class WorkerRequest extends EmployeeRequest {

    @NotNull(message = "Worker role must not be null")
   private WorkerRole workerRole;

    @NotNull(message = "Branch id must not be null")
   private Long branchId;

    private WorkerStatus workerStatus;
    private String score;

}
