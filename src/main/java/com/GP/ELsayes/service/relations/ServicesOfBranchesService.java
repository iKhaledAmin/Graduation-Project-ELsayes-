package com.GP.ELsayes.service.relations;

import com.GP.ELsayes.model.dto.relations.ServicesOfBranchesResponse;
import org.springframework.stereotype.Service;

@Service
public interface ServicesOfBranchesService {
    ServicesOfBranchesResponse addServiceToBranch(Long serviceId , Long branchId);
    ServicesOfBranchesResponse activateServiceInBranch(Long serviceId , Long branchId);
    ServicesOfBranchesResponse deactivateServiceInBranch(Long serviceId , Long branchId);
}
