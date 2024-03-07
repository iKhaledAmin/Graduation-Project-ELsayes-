package com.GP.ELsayes.service.relations.impl;

import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
import com.GP.ELsayes.model.entity.relations.OwnersOfRestrictedOwners;
import com.GP.ELsayes.model.enums.OperationType;
import com.GP.ELsayes.repository.relations.OwnersOfRestrictedOwnersRepo;
import com.GP.ELsayes.service.relations.OwnersOfRestrictedOwnersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class OwnersOfRestrictedOwnersServiceImpl implements OwnersOfRestrictedOwnersService {
    private final OwnersOfRestrictedOwnersRepo ownersOfRestrictedOwnersRepo;

    @Override
    public OwnersOfRestrictedOwners add(Owner oldOwner, Owner restrictedOwner, OperationType operationType) {

        OwnersOfRestrictedOwners ownersOfRestrictedOwners = new OwnersOfRestrictedOwners();
        ownersOfRestrictedOwners.setOldOwner(oldOwner);
        ownersOfRestrictedOwners.setRestrictedOwner(restrictedOwner);
        ownersOfRestrictedOwners.setOperationType(operationType);
        ownersOfRestrictedOwners.setOperationDate(new Date());

        return ownersOfRestrictedOwnersRepo.save(ownersOfRestrictedOwners);
    }
}
