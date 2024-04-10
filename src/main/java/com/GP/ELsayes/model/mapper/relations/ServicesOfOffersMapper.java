package com.GP.ELsayes.model.mapper.relations;

import com.GP.ELsayes.model.dto.relations.ServicesOfPackagesResponse;
import com.GP.ELsayes.model.entity.relations.ServicesOfPackage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServicesOfOffersMapper {
    @Mapping(target = "serviceName" , source = "entity.service.name")
   // @Mapping(target = "packageName" , source = "entity.packagesOfOrder.name")
    ServicesOfPackagesResponse toResponse(ServicesOfPackage entity);
}
