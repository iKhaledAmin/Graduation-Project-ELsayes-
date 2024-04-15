package com.GP.ELsayes.service;

import com.GP.ELsayes.model.dto.CheckOutResponse;
import com.GP.ELsayes.model.dto.FreeTrialCodeResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.WorkerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.WorkerResponse;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.GP.ELsayes.model.enums.WorkerStatus;
import com.GP.ELsayes.model.enums.roles.WorkerRole;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface WorkerService extends UserService, CrudService<WorkerRequest, Worker, WorkerResponse,Long> {




    public List<WorkerResponse> getAllByBranchId(Long branchId);
    public void recordVisitation(String carPlateNumber , Long branchId);
    public CheckOutResponse checkOut(String carPlateNumber, Long workerId);
    public void finishTask(String carPlateNumber, Long workerId);
    public  Integer getNumberOfWorkersByBranchId (Long branchId);
    public void updateWorkerStatus(Long workerId, WorkerStatus workerStatus);

    public  List<Worker> getAllAvailableWorkerByWorkerRoleOrderByScore(WorkerRole workerRole);

    public void changeWorkerStatus(Long workerId);

    public FreeTrialCodeResponse generateFreeTrialCode(Long workerId);

}
