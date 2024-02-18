package com.GP.ELsayes.service;

import com.GP.ELsayes.model.dto.BranchRequest;
import com.GP.ELsayes.model.dto.BranchResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.ManagerResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.WorkerResponse;
import com.GP.ELsayes.model.entity.Branch;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BranchService extends CrudService<BranchRequest, Branch, BranchResponse,Long>{
    List<WorkerResponse> getAllBranchWorkers(Long branchId);

    Branch getByManagerId(Long managerId);
    public BranchResponse getResponseByManagerId(Long managerId);
}
