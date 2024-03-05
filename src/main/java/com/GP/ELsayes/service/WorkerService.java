package com.GP.ELsayes.service;

import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.WorkerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.WorkerResponse;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WorkerService extends UserService, CrudService<WorkerRequest, Worker, WorkerResponse,Long> {

    public List<WorkerResponse> getAllByBranchId(Long branchId);
    public  Integer getNumberOfWorkersByBranchId (Long branchId);
}
