package com.GP.ELsayes.service;

import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.ManagerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.ManagerResponse;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ManagerService extends UserService , CrudService<ManagerRequest, Manager, ManagerResponse,Long>{

    public Manager getByBranchId(long branchId);

    public Manager getByOfferId(long offerId);
    public ManagerResponse getResponseByBranchId(Long branchId);
    public Optional<Manager> getIfExistByBranchId(Long managerId);
}
