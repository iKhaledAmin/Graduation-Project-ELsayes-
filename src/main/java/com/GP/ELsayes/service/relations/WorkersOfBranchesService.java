package com.GP.ELsayes.service.relations;

import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.GP.ELsayes.model.entity.relations.WorkersOfBranches;
import com.GP.ELsayes.model.enums.OperationType;
import org.springframework.stereotype.Service;

@Service
public interface WorkersOfBranchesService {

    WorkersOfBranches addWorkerToBranch(Worker worker , Branch branch);
    WorkersOfBranches updateWorkerOfBranch(Long workerId , Long branchId);

}
