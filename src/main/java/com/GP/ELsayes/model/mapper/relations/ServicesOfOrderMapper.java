package com.GP.ELsayes.model.mapper.relations;

import com.GP.ELsayes.model.dto.ServicesOfOrderResponse;
import com.GP.ELsayes.model.entity.relations.ServicesOfOrders;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServicesOfOrderMapper {

    @Mapping(source = "entity.service.name", target = "serviceName")
    @Mapping(source = "entity.service.price", target = "servicePrice")
    @Mapping(source = "entity.service.requiredTime", target = "requiredTime")
    ServicesOfOrderResponse toResponse(ServicesOfOrders entity);
}
