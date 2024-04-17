package com.GP.ELsayes.service.relations;

import com.GP.ELsayes.model.dto.relations.ServicesOfBranchesResponse;
import com.GP.ELsayes.model.entity.relations.ServicesOfBranches;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public interface ServicesOfBranchesService {
    ServicesOfBranchesResponse addServiceToBranch(Long serviceId , Long branchId);
    ServicesOfBranchesResponse activateServiceInBranch(Long serviceId , Long branchId);
    ServicesOfBranchesResponse deactivateServiceInBranch(Long serviceId , Long branchId);

    public Optional<ServicesOfBranches> getObjectByServiceIdAndBranchId(Long serviceId , Long branchId);

    public  ServicesOfBranches getByServiceIdAndBranchId(Long serviceId , Long branchId);

}
