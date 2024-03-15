package com.GP.ELsayes.service.relations;

import com.GP.ELsayes.model.dto.CustomerVisitationsResponse;
import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.entity.relations.CustomerVisitationsOfBranches;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface CustomerVisitationsOfBranchesService {
    CustomerVisitationsOfBranches add(Customer customer , Branch branch);
    public CustomerVisitationsOfBranches endVisitation(CustomerVisitationsOfBranches customerVisitation);
    CustomerVisitationsOfBranches getRecentByCarPlateNumberAndBranchId(String carPlateNumber , Long branchId);

    public List<CustomerVisitationsResponse> getResponseAllCurrentVisitationsInBranch(Long branchId);
    public List<CustomerVisitationsResponse> getResponseAllVisitationsInBranchByADate(Long branchId , Date date);
}
