package com.GP.ELsayes.model.mapper;

import com.GP.ELsayes.model.dto.ServicesOfBranchesResponse;
import com.GP.ELsayes.model.entity.relations.ServicesOfBranches;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ServicesOfBranchesMapper {
    ServicesOfBranches INSTANCE = Mappers.getMapper(ServicesOfBranches.class);

    @Mapping(target = "serviceName" , source = "entity.service.name")
    @Mapping(target = "branchName" , source = "entity.branch.name")
    ServicesOfBranchesResponse toResponse(ServicesOfBranches entity);
}
