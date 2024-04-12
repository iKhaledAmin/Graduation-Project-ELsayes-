package com.GP.ELsayes.model.mapper.relations;

import com.GP.ELsayes.model.dto.PackagesOfOrderResponse;
import com.GP.ELsayes.model.entity.relations.PackagesOfOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface PackagesOfOrderMapper {
    PackagesOfOrder INSTANCE = Mappers.getMapper(PackagesOfOrder.class);
    @Mapping(source = "entity.packageEntity.name", target = "packageName")
    @Mapping(source = "entity.packageEntity.currentPackagePrice", target = "packagePrice")
    @Mapping(source = "entity.packageEntity.originalTotalRequiredTime", target = "requiredTime")
    PackagesOfOrderResponse toResponse(PackagesOfOrder entity);
}
