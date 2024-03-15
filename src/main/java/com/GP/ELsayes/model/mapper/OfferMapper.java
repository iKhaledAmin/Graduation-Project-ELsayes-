package com.GP.ELsayes.model.mapper;

import com.GP.ELsayes.model.dto.OfferRequest;
import com.GP.ELsayes.model.dto.OfferResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.OwnerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.OwnerResponse;
import com.GP.ELsayes.model.entity.Offer;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
import com.GP.ELsayes.model.enums.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OfferMapper {
    OfferMapper INSTANCE = Mappers.getMapper(OfferMapper.class);

    Offer toEntity(OfferRequest request);

//    default Status mapOfferStatus(Offer offer) {
//        return offer.getOffersOfBranch().isEmpty() ? null : offer.getOffersOfBranch().get(0).getOfferStatus();
//    }
//
//    @Mapping(target = "offerStatus", expression = "java(mapOfferStatus(entity))")
    OfferResponse toResponse(Offer entity);

}