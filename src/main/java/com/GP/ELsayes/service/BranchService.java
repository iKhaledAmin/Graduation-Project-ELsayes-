package com.GP.ELsayes.service;

import com.GP.ELsayes.model.dto.BranchRequest;
import com.GP.ELsayes.model.dto.BranchResponse;
import com.GP.ELsayes.model.dto.CustomerVisitationsResponse;
import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.Offer;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface BranchService extends CrudService<BranchRequest, Branch, BranchResponse,Long>{

    public Branch getByManagerId(Long managerId);
    public BranchResponse getResponseByManagerId(Long managerId);

    Branch getByWorkerId(Long workerId);

    public List<Branch> getAllByServiceId(Long serviceId);
    public List<Branch> getAllByOfferId(Long offerId);
    public List<CustomerVisitationsResponse> getResponseAllCurrentVisitationsInBranch(Long branchId);
    public List<CustomerVisitationsResponse> getResponseAllVisitationsInBranchByADate(Long branchId, Date date);


}
