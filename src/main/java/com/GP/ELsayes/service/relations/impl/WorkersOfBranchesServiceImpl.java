package com.GP.ELsayes.service.relations.impl;

import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.GP.ELsayes.model.entity.relations.WorkersOfBranches;
import com.GP.ELsayes.model.enums.OperationType;
import com.GP.ELsayes.repository.relations.WorkersOfBranchesRepo;
import com.GP.ELsayes.service.ManagerService;
import com.GP.ELsayes.service.relations.WorkersOfBranchesService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class WorkersOfBranchesServiceImpl implements WorkersOfBranchesService {
    private final ManagerService managerService;
    private final WorkersOfBranchesRepo workersOfBranchesRepo;
    //private final WorkerService workerService;

    private WorkersOfBranches getByWorkerIdAndBranchId(Long workerId , Long branchId) {
        return workersOfBranchesRepo.findByWorkerIdAndBranchId(workerId,branchId).orElseThrow(
                () -> new NoSuchElementException("There is no worker with id = " + workerId + " in this branch")
        );
    }
    private WorkersOfBranches add(Worker worker, Branch branch, OperationType operationType) {
        Manager manager = managerService.getByBranchId(branch.getId());

        WorkersOfBranches workersOfBranch = new WorkersOfBranches();
        workersOfBranch.setWorker(worker);
        workersOfBranch.setBranch(branch);
        workersOfBranch.setStartAt(new Date());
        workersOfBranch.setManager(manager);
        workersOfBranch.setOperationType(operationType);
        workersOfBranch.setOperationDate(new Date());


        return workersOfBranchesRepo.save(workersOfBranch);
    }

    @SneakyThrows
    private WorkersOfBranches update(WorkersOfBranches workersOfBranches){
        WorkersOfBranches updatedWorkersOfBranch = workersOfBranches;
        WorkersOfBranches existedWorkersOfBranch = getByWorkerIdAndBranchId(
                workersOfBranches.getWorker().getId(),
                workersOfBranches.getBranch().getId()
        );

        Manager manager = managerService.getByBranchId(workersOfBranches.getId());


        BeanUtils.copyProperties(existedWorkersOfBranch,updatedWorkersOfBranch);
        updatedWorkersOfBranch.setOperationType(OperationType.UPDATE);
        updatedWorkersOfBranch.setOperationDate(new Date());

        return workersOfBranchesRepo.save(updatedWorkersOfBranch);
    }


    @Override
    public WorkersOfBranches addWorkerToBranch(Worker worker, Branch branch) {
        return add(
                worker,
                branch,
                OperationType.CREATE
        );
    }

    @Override
    public WorkersOfBranches updateWorkerOfBranch(Long workerId, Long branchId) {
        WorkersOfBranches updatedWorkersOfBranch = getByWorkerIdAndBranchId(
                workerId,
                branchId
        );
        return update(updatedWorkersOfBranch);
    }
}
