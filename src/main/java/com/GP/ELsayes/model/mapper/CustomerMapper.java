package com.GP.ELsayes.model.mapper;

import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerResponse;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerMapper {


    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    Customer toEntity(CustomerRequest request);

    @Mapping(target = "freeTrialCode" , source = "entity.customerFreeTrialCode.code")
    CustomerResponse toResponse(Customer entity);

}