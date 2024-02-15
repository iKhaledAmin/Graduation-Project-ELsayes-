package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.WorkerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.WorkerResponse;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.GP.ELsayes.model.enums.roles.UserRole;
import com.GP.ELsayes.model.mapper.WorkerMapper;
import com.GP.ELsayes.repository.WorkerRepo;
import com.GP.ELsayes.service.BranchService;
import com.GP.ELsayes.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class WorkerServiceImpl implements WorkerService {

    final private WorkerMapper workerMapper;
    final private WorkerRepo workerRepo;
    private final BranchService branchService;
    @Override
    public Worker getById(Long workerId) {
        return workerRepo.findById(workerId).orElseThrow(
                () -> new NoSuchElementException("There is no worker with id = " + workerId)
        );
    }

    @Override
    public WorkerResponse add(WorkerRequest workerRequest) {

        Worker worker = this.workerMapper.toEntity(workerRequest);
        worker.setUserRole(UserRole.WORKER);
        worker.setBranchWorkOn(this.branchService.getById(workerRequest.getBranchIdWorkOn()));
        return this.workerMapper.toResponse(this.workerRepo.save(worker));
    }

    @Override
    public List<WorkerResponse> getAll() {
        return workerRepo.findAll()
                .stream()
                .map(worker ->  workerMapper.toResponse(worker))
                .toList();
    }

    @Override
    public WorkerResponse update(WorkerRequest workerRequest, Long workerId) {
        Worker existedWorker = this.getById(workerId);
        existedWorker = this.workerMapper.toEntity(workerRequest);
        existedWorker.setId(workerId);
        return this.workerMapper.toResponse(workerRepo.save(existedWorker));
    }

    @Override
    public void delete(Long workerId) {
        getById(workerId);
        workerRepo.deleteById(workerId);
    }
}
