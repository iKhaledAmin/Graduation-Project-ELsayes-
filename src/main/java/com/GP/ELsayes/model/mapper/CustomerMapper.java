package com.GP.ELsayes.model.mapper;

import com.GP.ELsayes.model.dto.ServiceResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerResponse;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.entity.relations.ServicesOfBranches;
import com.GP.ELsayes.model.entity.relations.VisitationsOfBranches;
import com.GP.ELsayes.model.enums.Status;
import com.GP.ELsayes.service.relations.ServicesOfBranchesService;
import com.GP.ELsayes.service.relations.VisitationsOfBranchesService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface CustomerMapper {


    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    Customer toEntity(CustomerRequest request);

    @Mapping(target = "freeTrialCode" , source = "entity.customerFreeTrialCode.code")
    CustomerResponse toResponse(Customer entity);



    @Mapping(target = "currentBranchId", expression = "java(getCurrentBranchId(entity.getId(), visitationsOfBranchesService))")
    CustomerResponse toResponseAccordingToBranch(Customer entity, VisitationsOfBranchesService visitationsOfBranchesService);



    default Long getCurrentBranchId(Long customerId, VisitationsOfBranchesService visitationsOfBranchesService) {
        VisitationsOfBranches visitationsOfBranch = getVisitationsOfBranch(customerId, visitationsOfBranchesService);

        if (visitationsOfBranch != null) {
            return visitationsOfBranch.getBranch().getId();
        }

        return null;
    }



    default VisitationsOfBranches getVisitationsOfBranch(Long customerId, VisitationsOfBranchesService visitationsOfBranchesService) {
        Optional<VisitationsOfBranches> visitationsOfBranch = visitationsOfBranchesService.getCurrentVisitationByCustomerId(customerId);
        if (visitationsOfBranch.isPresent()) {
            return visitationsOfBranch.get();
        }
        return null;
    }


}