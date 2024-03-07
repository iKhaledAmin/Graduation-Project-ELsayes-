package com.GP.ELsayes.model.mapper.relations;

import com.GP.ELsayes.model.dto.relations.ServicesOfOffersResponse;
import com.GP.ELsayes.model.entity.relations.ServicesOfOffers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServicesOfOffersMapper {
    @Mapping(target = "serviceName" , source = "entity.service.name")
    @Mapping(target = "offerName" , source = "entity.offer.name")
    ServicesOfOffersResponse toResponse(ServicesOfOffers entity);
}
