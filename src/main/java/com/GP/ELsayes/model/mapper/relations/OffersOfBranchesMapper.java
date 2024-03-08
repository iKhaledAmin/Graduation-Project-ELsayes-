package com.GP.ELsayes.model.mapper.relations;


import com.GP.ELsayes.model.dto.relations.OffersOfBranchesResponse;
import com.GP.ELsayes.model.entity.relations.OffersOfBranches;
import com.GP.ELsayes.model.entity.relations.ServicesOfBranches;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OffersOfBranchesMapper {
    OffersOfBranches INSTANCE = Mappers.getMapper(OffersOfBranches.class);

    @Mapping(target = "offerName" , source = "entity.offer.name")
    @Mapping(target = "branchName" , source = "entity.branch.name")
    OffersOfBranchesResponse toResponse(OffersOfBranches entity);
}
