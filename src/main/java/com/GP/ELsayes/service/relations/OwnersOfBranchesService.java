package com.GP.ELsayes.service.relations;

import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
import com.GP.ELsayes.model.entity.relations.OwnersOfBranches;
import com.GP.ELsayes.model.enums.OperationType;
import org.springframework.stereotype.Service;

@Service
public interface OwnersOfBranchesService
        extends CrudOfRelationsService<OwnersOfBranches, Owner, Branch, OperationType,Long> {
}
