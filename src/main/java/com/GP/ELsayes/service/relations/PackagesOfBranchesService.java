package com.GP.ELsayes.service.relations;


import com.GP.ELsayes.model.dto.relations.PackageOfBranchesResponse;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.relations.PackagesOfBranches;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PackagesOfBranchesService {
    PackagesOfBranches getByPackageIdAndBranchId(Long packageId, Long branchId);
    public List<ServiceEntity> getAllPackageServicesNotAvailableInBranch(Long packageId, Long branchId);
    PackageOfBranchesResponse addPackageToBranch(Long packageId , Long branchId);
    PackageOfBranchesResponse activatePackageInBranch(Long packageId , Long branchId);
    PackageOfBranchesResponse deactivatePackageInBranch(Long packageId , Long branchId);

    PackagesOfBranches update(PackagesOfBranches  packagesOfBranch);
}

