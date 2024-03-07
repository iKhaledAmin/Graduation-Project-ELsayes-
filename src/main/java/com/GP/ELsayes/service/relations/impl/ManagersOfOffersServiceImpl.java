package com.GP.ELsayes.service.relations.impl;

import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.Offer;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.GP.ELsayes.model.entity.relations.ManagersOfOffers;
import com.GP.ELsayes.model.entity.relations.ManagersOfServices;
import com.GP.ELsayes.model.entity.relations.WorkersOfBranches;
import com.GP.ELsayes.model.enums.OperationType;
import com.GP.ELsayes.repository.relations.ManagersOfOffersRepo;
import com.GP.ELsayes.service.relations.ManagersOfOffersService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class ManagersOfOffersServiceImpl implements ManagersOfOffersService {

    private final ManagersOfOffersRepo managersOfOffersRepo;
//    @Override
//    public ManagersOfOffers add(Manager manager, Offer offer, OperationType operationType) {
//        ManagersOfOffers managersOfOffers = new ManagersOfOffers();
//        managersOfOffers.setManager(manager);
//        managersOfOffers.setOffer(offer);
//        managersOfOffers.setOperationType(operationType);
//        managersOfOffers.setOperationDate(new Date());
//
//        return this.managersOfOffersRepo.save(managersOfOffers);
//    }


    private ManagersOfOffers getByManagerIdAndOfferId(Long managerId , Long offerId) {
        return managersOfOffersRepo.findByManagerIdAndOfferId(managerId,offerId).orElseThrow(
                () -> new NoSuchElementException("There is no offer with id = " + offerId + " managed by this manager")
        );
    }
    private ManagersOfOffers add(Manager manager, Offer offer, OperationType operationType) {

        ManagersOfOffers managersOfOffer = new ManagersOfOffers();
        managersOfOffer.setManager(manager);
        managersOfOffer.setOffer(offer);
        managersOfOffer.setOperationType(operationType);
        managersOfOffer.setOperationDate(new Date());

        return managersOfOffersRepo.save(managersOfOffer);
    }

    @SneakyThrows
    private ManagersOfOffers update(ManagersOfOffers managersOfOffers){

        ManagersOfOffers updatedManagersOfOffer = managersOfOffers;
        ManagersOfOffers existedManagersOfOffer = getByManagerIdAndOfferId(
                managersOfOffers.getManager().getId(),
                managersOfOffers.getOffer().getId()
        );

        BeanUtils.copyProperties(updatedManagersOfOffer,existedManagersOfOffer);
        updatedManagersOfOffer.setOperationType(OperationType.UPDATE);
        updatedManagersOfOffer.setOperationDate(new Date());

        return managersOfOffersRepo.save(updatedManagersOfOffer);
    }


    @Override
    public ManagersOfOffers addManagerToOffer(Manager manager, Offer offer) {
        return add(
                manager,
                offer,
                OperationType.CREATE
        );
    }

    @Override
    public ManagersOfOffers updateManagerToOffer(Long managerId, Long offerId) {
        ManagersOfOffers updatedManagersOfOffer = getByManagerIdAndOfferId(
                managerId,
                offerId
        );
        return update(updatedManagersOfOffer);
    }
}
