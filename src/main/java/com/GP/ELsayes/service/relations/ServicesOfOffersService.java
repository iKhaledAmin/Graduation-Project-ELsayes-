package com.GP.ELsayes.service.relations;

import com.GP.ELsayes.model.dto.relations.ServicesOfOffersResponse;
import org.springframework.stereotype.Service;

@Service
public interface ServicesOfOffersService {
    public void handleAvailabilityOfOfferInAllBranches(Long offerId , Long serviceId);
    ServicesOfOffersResponse addServiceToOffer(Long serviceId , Long offerId);

}
