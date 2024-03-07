package com.GP.ELsayes.service.relations.impl;

import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
import com.GP.ELsayes.model.entity.relations.ManagersOfServices;
import com.GP.ELsayes.model.entity.relations.OwnersOfBranches;
import com.GP.ELsayes.model.enums.OperationType;
import com.GP.ELsayes.repository.relations.OwnersOfBranchesRepo;
import com.GP.ELsayes.service.relations.OwnersOfBranchesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class OwnersOfBranchesServiceImpl implements OwnersOfBranchesService {

    private final OwnersOfBranchesRepo ownersOfBranchesRepo;
    @Override
    public OwnersOfBranches add(Owner owner, Branch branch, OperationType operationType) {
        OwnersOfBranches ownersOfBranches = new OwnersOfBranches();
        ownersOfBranches.setOwner(owner);
        ownersOfBranches.setBranch(branch);
        ownersOfBranches.setOperationType(operationType);
        ownersOfBranches.setOperationDate(new Date());

        return this.ownersOfBranchesRepo.save(ownersOfBranches);
    }
}
