package com.GP.ELsayes.model.mapper;

import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.ManagerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.ManagerResponse;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ManagerMapper {
    //ManagerMapper INSTANCE = Mappers.getMapper(ManagerMapper.class);
    ManagerRequest toRequest(Manager entity);

    Manager toEntity(ManagerRequest request);

    ManagerResponse toResponse(Manager responses);
}
