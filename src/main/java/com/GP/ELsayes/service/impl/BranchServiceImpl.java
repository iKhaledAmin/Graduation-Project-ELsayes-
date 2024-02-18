package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.BranchRequest;
import com.GP.ELsayes.model.dto.BranchResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.ManagerResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.WorkerResponse;
import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.mapper.BranchMapper;
import com.GP.ELsayes.model.mapper.WorkerMapper;
import com.GP.ELsayes.repository.BranchRepo;
import com.GP.ELsayes.repository.WorkerRepo;
import com.GP.ELsayes.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BranchServiceImpl implements BranchService {

    private final BranchMapper branchMapper;
    private final BranchRepo branchRepo;
    private final WorkerRepo workerRepo;
    private final WorkerMapper workerMapper;

    @Override
    public BranchResponse add(BranchRequest branchRequest) {
        Branch branch = this.branchMapper.toEntity(branchRequest);
        return this.branchMapper.toResponse(this.branchRepo.save(branch));
    }

    @Override
    public BranchResponse update(BranchRequest branchRequest, Long branchId) {
        Branch existedBranch = this.getById(branchId);
        existedBranch = this.branchMapper.toEntity(branchRequest);
        existedBranch.setId(branchId);
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

    @Override
    public List<WorkerResponse> getAllBranchWorkers(Long branchId) {
        return branchRepo.getById(branchId).getWorkers()
                .stream().map(worker ->  workerMapper.toResponse(worker))
                .collect(Collectors.toList());
    }




}
