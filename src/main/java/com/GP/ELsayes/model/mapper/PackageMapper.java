package com.GP.ELsayes.model.mapper;

import com.GP.ELsayes.model.dto.PackageRequest;
import com.GP.ELsayes.model.dto.PackageResponse;
import com.GP.ELsayes.model.entity.Package;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OfferMapper {
    OfferMapper INSTANCE = Mappers.getMapper(OfferMapper.class);

    Package toEntity(PackageRequest request);

//    default Status mapOfferStatus(Offer offer) {
//        return offer.getOffersOfBranch().isEmpty() ? null : offer.getOffersOfBranch().get(0).getOfferStatus();
//    }
//
//    @Mapping(target = "offerStatus", expression = "java(mapOfferStatus(entity))")
    PackageResponse toResponse(Package entity);

}