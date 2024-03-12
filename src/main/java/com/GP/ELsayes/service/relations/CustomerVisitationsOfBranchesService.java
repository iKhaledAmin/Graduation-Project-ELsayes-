package com.GP.ELsayes.service.relations;

import com.GP.ELsayes.model.dto.relations.CustomerVisitationsOfBranchesRequest;
import com.GP.ELsayes.model.entity.relations.CustomerVisitationsOfBranches;
import org.springframework.stereotype.Service;

@Service
public interface CustomerVisitationsOfBranchesService {
    CustomerVisitationsOfBranches add(CustomerVisitationsOfBranchesRequest customerVisitationRequest);
}