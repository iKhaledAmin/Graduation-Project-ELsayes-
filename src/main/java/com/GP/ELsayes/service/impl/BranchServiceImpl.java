package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.BranchRequest;
import com.GP.ELsayes.model.dto.BranchResponse;
import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.relations.OwnersOfBranches;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
import com.GP.ELsayes.model.enums.OperationType;
import com.GP.ELsayes.model.mapper.BranchMapper;
import com.GP.ELsayes.repository.BranchRepo;
import com.GP.ELsayes.repository.relations.OwnersOfBranchesRepo;
import com.GP.ELsayes.service.BranchService;
import com.GP.ELsayes.service.OwnerService;
import com.GP.ELsayes.service.relations.OwnersOfBranchesService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class BranchServiceImpl implements BranchService {

    private final BranchMapper branchMapper;
    private final BranchRepo branchRepo;
    private final OwnerService ownerService;
    private final OwnersOfBranchesService ownersOfBranchesService;

    @Override
    public BranchResponse add(BranchRequest branchRequest) {
        Branch branch = this.branchMapper.toEntity(branchRequest);
        branch = this.branchRepo.save(branch);

        Owner owner = ownerService.getById(branchRequest.getOwnerId());
        OwnersOfBranches ownersOfBranches = ownersOfBranchesService.save(
                owner,
                branch,
                OperationType.CREATE
        );

        return this.branchMapper.toResponse(branch);
    }

    @SneakyThrows
    @Override
    public BranchResponse update(BranchRequest branchRequest, Long branchId) {

        Branch existedBranch = this.getById(branchId);
        Branch updatedBranch = this.branchMapper.toEntity(branchRequest);

        updatedBranch.setId(branchId);
        BeanUtils.copyProperties(existedBranch,updatedBranch);
        updatedBranch = branchRepo.save(updatedBranch);


        Owner owner = ownerService.getById(branchRequest.getOwnerId());
        OwnersOfBranches ownersOfBranches = ownersOfBranchesService.save(
                owner,
                updatedBranch,
                OperationType.UPDATE
        );

        return this.branchMapper.toResponse(updatedBranch);
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
