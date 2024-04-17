package com.GP.ELsayes.model.mapper.relations;


import com.GP.ELsayes.model.dto.relations.PackageOfBranchesResponse;
import com.GP.ELsayes.model.entity.relations.PackagesOfBranches;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PackagesOfBranchesMapper {
    PackagesOfBranches INSTANCE = Mappers.getMapper(PackagesOfBranches.class);

    @Mapping(target = "branchName" , source = "entity.branch.name")
    @Mapping(target = "packageName" , source = "entity.packageEntity.name")
    PackageOfBranchesResponse toResponse(PackagesOfBranches entity);
}
