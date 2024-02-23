package com.GP.ELsayes.service.relations;

import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
import com.GP.ELsayes.model.entity.relations.OwnersOfManagers;
import com.GP.ELsayes.model.enums.OperationType;

public interface OwnersOfManagersService
        extends CrudOfRelationsService<OwnersOfManagers, Owner, Manager, OperationType,Long> {
}
