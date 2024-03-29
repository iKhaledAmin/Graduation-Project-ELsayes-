package com.GP.ELsayes.service;


import com.GP.ELsayes.model.dto.BranchRequest;
import com.GP.ELsayes.model.dto.BranchResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.ManagerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.ManagerResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.OwnerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.OwnerResponse;
import com.GP.ELsayes.model.dto.relations.VisitationsOfBranchesResponse;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface OwnerService extends UserService , CrudService<OwnerRequest , Owner , OwnerResponse, Long >{
    public Owner getByManagerId(Long managerId);
    public ManagerResponse addManager(ManagerRequest managerRequest);
    public ManagerResponse updateManager(ManagerRequest managerRequest, Long managerId);
    public void deleteManager(Long managerId);
    public List<ManagerResponse> getAllManager();
    public ManagerResponse getResponseManagerById(Long managerId);
    public ManagerResponse getResponseManagerByBranchId(Long branchId);


    public BranchResponse addBranch(BranchRequest branchRequest);
    public BranchResponse updateBranch(BranchRequest branchRequest, Long branchId);
    public void deleteBranch(Long branchId);
    public List<BranchResponse> getAllBranches();
    public BranchResponse getBranchResponseById(Long branchId);
    public List<VisitationsOfBranchesResponse> getResponseAllCurrentVisitationsInBranch(Long branchId);
    public List<VisitationsOfBranchesResponse> getResponseAllVisitationsInBranchByADate(Long branchId, Date date);

}
