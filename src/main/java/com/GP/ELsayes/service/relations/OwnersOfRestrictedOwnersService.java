package com.GP.ELsayes.service.relations;

import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
import com.GP.ELsayes.model.entity.relations.OwnersOfRestrictedOwners;
import com.GP.ELsayes.model.enums.OperationType;
import org.springframework.stereotype.Service;

@Service
public interface OwnersOfRestrictedOwnersService extends
        CrudOfRelationsService <OwnersOfRestrictedOwners,Owner,Owner, OperationType,Long> {
}
