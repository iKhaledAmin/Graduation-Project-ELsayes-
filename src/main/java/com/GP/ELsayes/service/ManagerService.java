package com.GP.ELsayes.service;

import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.ManagerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.ManagerResponse;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import org.springframework.stereotype.Service;

@Service
public interface ManagerService extends CrudService<ManagerRequest, Manager, ManagerResponse,Long>{
   Manager getByBranchId(long branchId);

}
