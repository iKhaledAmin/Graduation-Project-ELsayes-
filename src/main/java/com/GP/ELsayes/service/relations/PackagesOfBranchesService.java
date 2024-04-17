package com.GP.ELsayes.service.relations;


import com.GP.ELsayes.model.dto.relations.PackageOfBranchesResponse;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.relations.PackagesOfBranches;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PackagesOfBranchesService {
    Optional<PackagesOfBranches> getObjectByPackageIdAndBranchId(Long packageId, Long branchId);
    public PackagesOfBranches getByPackageIdAndBranchId(Long packageId , Long branchId);
    public List<ServiceEntity> getAllPackageServicesNotAvailableInBranch(Long packageId, Long branchId);
    PackageOfBranchesResponse addPackageToBranch(Long packageId , Long branchId);
    PackageOfBranchesResponse activatePackageInBranch(Long packageId , Long branchId);
    PackageOfBranchesResponse deactivatePackageInBranch(Long packageId , Long branchId);

    PackagesOfBranches update(PackagesOfBranches  packagesOfBranch);
}

