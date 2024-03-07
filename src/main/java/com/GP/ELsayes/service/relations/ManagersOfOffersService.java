package com.GP.ELsayes.service.relations;


import com.GP.ELsayes.model.entity.Offer;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.entity.relations.ManagersOfOffers;
import org.springframework.stereotype.Service;

@Service
public interface ManagersOfOffersService {
    ManagersOfOffers addManagerToOffer(Manager manager , Offer offer);
    ManagersOfOffers updateManagerToOffer(Long managerId , Long offerId);
}
