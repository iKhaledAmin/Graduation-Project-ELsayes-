package com.GP.ELsayes.model.mapper;

import com.GP.ELsayes.model.dto.ServiceRequest;
import com.GP.ELsayes.model.dto.ServiceResponse;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.enums.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    ServiceMapper INSTANCE = Mappers.getMapper(ServiceMapper.class);

    ServiceEntity toEntity(ServiceRequest request);

    default Status mapServiceStatus(ServiceEntity serviceEntity) {
        return serviceEntity.getServicesOfBranch().isEmpty() ? null : serviceEntity.getServicesOfBranch().get(0).getServiceStatus();
    }

    @Mapping(target = "serviceStatus", expression = "java(mapServiceStatus(entity))")
    ServiceResponse toResponse(ServiceEntity entity);

}
