package com.GP.ELsayes.service.relations;

import com.GP.ELsayes.model.dto.relations.ServicesOfBranchesResponse;
import com.GP.ELsayes.model.dto.relations.ServicesOfOffersResponse;
import org.springframework.stereotype.Service;

@Service
public interface ServicesOfOffersService {
    ServicesOfOffersResponse addServiceToOffer(Long serviceId , Long offerId);
}
