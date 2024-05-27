package com.GP.ELsayes.model.mapper;

import com.GP.ELsayes.model.dto.PackageRequest;
import com.GP.ELsayes.model.dto.PackageResponse;
import com.GP.ELsayes.model.dto.ServiceResponse;
import com.GP.ELsayes.model.entity.Package;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.relations.PackagesOfBranches;
import com.GP.ELsayes.model.enums.Status;
import com.GP.ELsayes.service.relations.PackagesOfBranchesService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface PackageMapper {
    PackageMapper INSTANCE = Mappers.getMapper(PackageMapper.class);

    Package toEntity(PackageRequest request);
    Package toEntity(PackageResponse response);


    PackageResponse toResponse(Package entity);


    // Map ServiceEntity to ServiceResponse with availability in a specific branch
    @Mapping(target = "availableInBranch", expression = "java(isAvailableInBranch(entity.getId(), branchId, packagesOfBranchesService))")
    PackageResponse toResponseAccordingToBranch(Package entity, Long branchId, PackagesOfBranchesService packagesOfBranchesService);



    // Custom method to determine availability based on serviceStatus
    default Boolean isAvailableInBranch(Long packageId, Long branchId, PackagesOfBranchesService packagesOfBranchesService) {
        PackagesOfBranches packagesOfBranch = getPackagesOfBranches(packageId, branchId, packagesOfBranchesService);
        if (packagesOfBranch != null) {

            Status serviceStatus = packagesOfBranch.getPackageStatus();
            return serviceStatus == Status.AVAILABLE ? true : false;
        }

        return false;
    }



    default PackagesOfBranches getPackagesOfBranches(Long packageId, Long branchId, PackagesOfBranchesService packagesOfBranchesService) {
        Optional<PackagesOfBranches> packagesOfBranch = packagesOfBranchesService.getObjectByPackageIdAndBranchId(packageId,branchId);
        if (packagesOfBranch.isPresent()) {
            return packagesOfBranch.get();
        }
        return null;
    }
}