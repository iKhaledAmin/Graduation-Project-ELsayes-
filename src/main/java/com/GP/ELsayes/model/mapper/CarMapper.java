package com.GP.ELsayes.model.mapper;

import com.GP.ELsayes.model.dto.CarRequest;
import com.GP.ELsayes.model.dto.CarResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerResponse;
import com.GP.ELsayes.model.entity.Car;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CarMapper {


    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    Car toEntity(CarRequest request);

    CarResponse toResponse(Car entity);
}
