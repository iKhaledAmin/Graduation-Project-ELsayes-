package com.GP.ELsayes.model.mapper.relations;

import com.GP.ELsayes.model.dto.CustomerVisitationsResponse;
import com.GP.ELsayes.model.dto.relations.OffersOfBranchesResponse;
import com.GP.ELsayes.model.entity.relations.CustomerVisitationsOfBranches;
import com.GP.ELsayes.model.entity.relations.OffersOfBranches;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerVisitationsMapper {
    CustomerVisitationsOfBranches INSTANCE = Mappers.getMapper(CustomerVisitationsOfBranches.class);




    @Mapping(target = "firstName" , source = "entity.customer.firstName")
    @Mapping(target = "lastName" , source = "entity.customer.lastName")
    @Mapping(target = "carPlateNumber" , source = "entity.car.carPlateNumber")
    CustomerVisitationsResponse toResponse(CustomerVisitationsOfBranches entity);

}
