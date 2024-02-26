package com.GP.ELsayes.service.relations.impl;

import com.GP.ELsayes.model.entity.Offer;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.entity.relations.ManagersOfOffers;
import com.GP.ELsayes.model.entity.relations.ManagersOfServices;
import com.GP.ELsayes.model.enums.OperationType;
import com.GP.ELsayes.repository.relations.ManagersOfOffersRepo;
import com.GP.ELsayes.service.relations.ManagersOfOffersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class ManagersOfOffersServiceImpl implements ManagersOfOffersService {
    private final ManagersOfOffersRepo managersOfOffersRepo;
    @Override
    public ManagersOfOffers save(Manager manager, Offer offer, OperationType operationType) {
        ManagersOfOffers managersOfOffers = new ManagersOfOffers();
        managersOfOffers.setManager(manager);
        managersOfOffers.setOffer(offer);
        managersOfOffers.setOperationType(operationType);
        managersOfOffers.setOperationDate(new Date());

        return this.managersOfOffersRepo.save(managersOfOffers);
    }
}
