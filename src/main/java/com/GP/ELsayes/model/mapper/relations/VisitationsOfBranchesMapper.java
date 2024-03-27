package com.GP.ELsayes.model.mapper.relations;

import com.GP.ELsayes.model.dto.relations.VisitationsOfBranchesResponse;
import com.GP.ELsayes.model.entity.relations.VisitationsOfBranches;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface VisitationsOfBranchesMapper {
    VisitationsOfBranches INSTANCE = Mappers.getMapper(VisitationsOfBranches.class);




    @Mapping(target = "firstName" , source = "entity.customer.firstName")
    @Mapping(target = "lastName" , source = "entity.customer.lastName")
    @Mapping(target = "carPlateNumber" , source = "entity.car.carPlateNumber")
    VisitationsOfBranchesResponse toResponse(VisitationsOfBranches entity);

}
