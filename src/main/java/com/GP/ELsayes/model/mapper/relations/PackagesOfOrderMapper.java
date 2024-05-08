package com.GP.ELsayes.model.mapper.relations;

import com.GP.ELsayes.model.dto.PackagesOfOrderResponse;
import com.GP.ELsayes.model.dto.ServicesOfOrderResponse;
import com.GP.ELsayes.model.entity.Package;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.relations.PackagesOfOrder;
import com.GP.ELsayes.service.PackageService;
import com.GP.ELsayes.service.ServiceService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface PackagesOfOrderMapper {
    //PackagesOfOrder INSTANCE = Mappers.getMapper(PackagesOfOrder.class);

    @Mapping(source = "entity.packageEntity.name", target = "packageName")
    @Mapping(source = "entity.packageEntity.currentPackagePrice", target = "packagePrice")
    @Mapping(source = "entity.packageEntity.originalTotalRequiredTime", target = "requiredTime")
    @Mapping(source = "entity.packageEntity.image", target = "image")
    PackagesOfOrderResponse toResponse(PackagesOfOrder entity);



//    @Mapping(source = "entity.name", target = "packageName")
//    @Mapping(source = "entity.image", target = "image")
//    @Mapping(source = "entity.currentPackagePrice", target = "packagePrice")
//    @Mapping(source = "entity.originalTotalRequiredTime", target = "requiredTime")

    @Mapping(source = "entity.packageEntity.name", target = "packageName")
    @Mapping(source = "entity.packageEntity.currentPackagePrice", target = "packagePrice")
    @Mapping(source = "entity.packageEntity.originalTotalRequiredTime", target = "requiredTime")
    @Mapping(source = "entity.packageEntity.image", target = "image")
    @Mapping(target = "availableInBranch", expression = "java(isAvailableInBranch(entity.getPackageEntity().getId(), branchId, packageService))")
    PackagesOfOrderResponse toResponseAccordingToBranch(PackagesOfOrder entity, Long branchId, PackageService packageService);



    default Boolean isAvailableInBranch(Long packageId, Long branchId, PackageService packageService) {
        return packageService.isAvailableInBranch(packageId,branchId);
    }
}
