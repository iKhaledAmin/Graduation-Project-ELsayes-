package com.GP.ELsayes.service.relations.impl;

import com.GP.ELsayes.model.entity.Package;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.entity.relations.ManagersOfPackages;
import com.GP.ELsayes.model.enums.OperationType;
import com.GP.ELsayes.repository.relations.ManagersOfOffersRepo;
import com.GP.ELsayes.service.relations.ManagersOfPackagesService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class ManagersOfPackagesServiceImpl implements ManagersOfPackagesService {

    private final ManagersOfOffersRepo managersOfOffersRepo;

    private ManagersOfPackages getByManagerIdAndOfferId(Long managerId , Long offerId) {
        return managersOfOffersRepo.findByManagerIdAndPackageId(managerId,offerId).orElseThrow(
                () -> new NoSuchElementException("There is no offer with id = " + offerId + " managed by this manager")
        );
    }
    private ManagersOfPackages add(Manager manager, Package aPackage, OperationType operationType) {

        ManagersOfPackages managersOfOffer = new ManagersOfPackages();
        managersOfOffer.setManager(manager);
        managersOfOffer.setPackageEntity(aPackage);
        managersOfOffer.setOperationType(operationType);
        managersOfOffer.setOperationDate(new Date());

        return managersOfOffersRepo.save(managersOfOffer);
    }

    @SneakyThrows
    private ManagersOfPackages update(ManagersOfPackages managersOfPackages){

        ManagersOfPackages updatedManagersOfOffer = managersOfPackages;
        ManagersOfPackages existedManagersOfOffer = getByManagerIdAndOfferId(
                managersOfPackages.getManager().getId(),
                managersOfPackages.getPackageEntity().getId()
        );

        BeanUtils.copyProperties(updatedManagersOfOffer,existedManagersOfOffer);
        updatedManagersOfOffer.setOperationType(OperationType.UPDATE);
        updatedManagersOfOffer.setOperationDate(new Date());

        return managersOfOffersRepo.save(updatedManagersOfOffer);
    }


    @Override
    public ManagersOfPackages addManagerToPackage(Manager manager, Package aPackage) {
        return add(
                manager,
                aPackage,
                OperationType.CREATE
        );
    }

    @Override
    public ManagersOfPackages updateManagerToPackage(Long managerId, Long offerId) {
        ManagersOfPackages updatedManagersOfOffer = getByManagerIdAndOfferId(
                managerId,
                offerId
        );
        return update(updatedManagersOfOffer);
    }
}
