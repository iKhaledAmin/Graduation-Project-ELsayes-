package com.GP.ELsayes.service.relations.impl;

import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
import com.GP.ELsayes.model.entity.relations.OwnersOfManagers;
import com.GP.ELsayes.model.entity.relations.OwnersOfRestrictedOwners;
import com.GP.ELsayes.model.enums.OperationType;
import com.GP.ELsayes.repository.relations.OwnersOfManagersRepo;
import com.GP.ELsayes.service.relations.OwnersOfManagersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class OwnersOfManagersServiceImpl implements OwnersOfManagersService {

    private final OwnersOfManagersRepo ownersOfManagersRepo;
    @Override
    public OwnersOfManagers save(Owner owner, Manager manager, OperationType operationType) {
        OwnersOfManagers ownersOfManagers = new OwnersOfManagers();
        ownersOfManagers.setManager(manager);
        ownersOfManagers.setOwner(owner);
        ownersOfManagers.setOperationType(operationType);
        ownersOfManagers.setOperationDate(new Date());

        return ownersOfManagersRepo.save(ownersOfManagers);
    }
}
