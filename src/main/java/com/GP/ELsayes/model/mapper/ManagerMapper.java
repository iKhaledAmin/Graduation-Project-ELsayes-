package com.GP.ELsayes.model.mapper;

import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.ManagerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.ManagerResponse;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ManagerMapper {

    ManagerMapper INSTANCE = Mappers.getMapper(ManagerMapper.class);

    Manager toEntity(ManagerRequest request);

    ManagerResponse toResponse(Manager entity);
}
