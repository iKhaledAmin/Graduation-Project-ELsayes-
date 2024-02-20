package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.BranchRequest;
import com.GP.ELsayes.model.dto.BranchResponse;
import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.OwnersOfBranches;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
import com.GP.ELsayes.model.enums.CrudType;
import com.GP.ELsayes.model.mapper.BranchMapper;
import com.GP.ELsayes.repository.BranchRepo;
import com.GP.ELsayes.repository.OwnersOfBranchesRepo;
import com.GP.ELsayes.service.BranchService;
import com.GP.ELsayes.service.OwnerService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class BranchServiceImpl implements BranchService {

    private final BranchMapper branchMapper;
    private final BranchRepo branchRepo;
    private final OwnerService ownerService;


    private final OwnersOfBranches ownersOfBranches = new OwnersOfBranches();
    private final OwnersOfBranchesRepo ownersOfBranchesRepo;

    @Override
    public BranchResponse add(BranchRequest branchRequest) {
        Branch branch = this.branchMapper.toEntity(branchRequest);
       // branch.setOwner(ownerService.getById(branchRequest.getOwnerId()));



        Owner owner = ownerService.getById(branchRequest.getOwnerId());
        ownersOfBranches.setOwner(owner);
        ownersOfBranches.setBranch(branch);
        ownersOfBranches.setOperationDate(new Date());
        ownersOfBranches.setOperationType(CrudType.CREATE);

        ownersOfBranchesRepo.save(ownersOfBranches);


        return this.branchMapper.toResponse(this.branchRepo.save(branch));
    }

    @SneakyThrows
    @Override
    public BranchResponse update(BranchRequest branchRequest, Long branchId) {

        Branch existedBranch = this.getById(branchId);
        Branch updatedBranch = this.branchMapper.toEntity(branchRequest);


        updatedBranch.setId(branchId);
        BeanUtils.copyProperties(existedBranch,updatedBranch);


        Owner owner = ownerService.getById(branchRequest.getOwnerId());
        ownersOfBranches.setOwner(owner);
        ownersOfBranches.setBranch(updatedBranch);
        ownersOfBranches.setOperationDate(new Date());
        ownersOfBranches.setOperationType(CrudType.CREATE);
        ownersOfBranchesRepo.save(ownersOfBranches);


        return this.branchMapper.toResponse(branchRepo.save(existedBranch));
    }

    @Override
    public void delete(Long branchId) {
        getById(branchId);
        branchRepo.deleteById(branchId);
    }

    @Override
    public List<BranchResponse> getAll() {
        return branchRepo.findAll()
                .stream()
                .map(branch ->  branchMapper.toResponse(branch))
                .toList();
    }

    @Override
    public Branch getById(Long branchId) {
        return branchRepo.findById(branchId).orElseThrow(
                () -> new NoSuchElementException("There are no branch with id = " + branchId)
        );
    }

    @Override
    public Branch getByManagerId(Long managerId) {
        return branchRepo.findByManagerId(managerId).orElseThrow(
                () -> new NoSuchElementException("There is no branch with manager id = " + managerId)
        );
    }


    @Override
    public BranchResponse getResponseById(Long branchId) {
        return branchMapper.toResponse(getById(branchId));
    }

    @Override
    public BranchResponse getResponseByManagerId(Long managerId) {
        return branchMapper.toResponse(getByManagerId(managerId));
    }






}
