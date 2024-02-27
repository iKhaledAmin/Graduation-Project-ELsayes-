package com.GP.ELsayes.model.mapper;

import com.GP.ELsayes.model.dto.FreeTrialCodeRequest;
import com.GP.ELsayes.model.dto.FreeTrialCodeResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerResponse;
import com.GP.ELsayes.model.entity.FreeTrialCode;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FreeTrialCodeMapper {
    FreeTrialCodeMapper INSTANCE = Mappers.getMapper(FreeTrialCodeMapper.class);

    FreeTrialCode toEntity(FreeTrialCodeRequest request);

    @Mapping(target = "workerId" , source = "entity.worker.id")
    @Mapping(target = "customerId" , source = "entity.customer.id")
    FreeTrialCodeResponse toResponse(FreeTrialCode entity);

    FreeTrialCodeRequest toRequest(FreeTrialCode entity );

}
