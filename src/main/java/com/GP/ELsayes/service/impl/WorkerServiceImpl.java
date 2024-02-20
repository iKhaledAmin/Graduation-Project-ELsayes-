package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.WorkerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.WorkerResponse;
import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.GP.ELsayes.model.enums.roles.UserRole;
import com.GP.ELsayes.model.mapper.WorkerMapper;
import com.GP.ELsayes.repository.WorkerRepo;
import com.GP.ELsayes.service.BranchService;
import com.GP.ELsayes.service.ManagerService;
import com.GP.ELsayes.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WorkerServiceImpl implements WorkerService {

    private final  WorkerMapper workerMapper;
    private final  WorkerRepo workerRepo;
    private final BranchService branchService;
    private final ManagerService managerService;


    @Override
    public WorkerResponse add(WorkerRequest workerRequest) {

        Worker worker = this.workerMapper.toEntity(workerRequest);
        worker.setUserRole(UserRole.WORKER);
        worker.setDateOfEmployment(new Date());

        Branch branch = this.branchService.getById(workerRequest.getBranchId());
        worker.setBranch(branch);
        worker.setManager(branch.getManager());
        return this.workerMapper.toResponse(this.workerRepo.save(worker));
    }



    @Override
    public WorkerResponse update(WorkerRequest workerRequest, Long workerId) {
        Worker existedWorker = this.getById(workerId);
        Worker updatedWorker = this.workerMapper.toEntity(workerRequest);


        updatedWorker.setId(workerId);
        updatedWorker.setDateOfEmployment(existedWorker.getDateOfEmployment());
        updatedWorker.setUserRole(existedWorker.getUserRole());
        //updatedWorker.setBranch(this.branchService.getById(workerRequest.getBranchId()));
        Branch branch = this.branchService.getById(workerRequest.getBranchId());
        updatedWorker.setBranch(branch);
        updatedWorker.setManager(branch.getManager());


        try {
            BeanUtils.copyProperties(existedWorker,updatedWorker);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }


        return this.workerMapper.toResponse(workerRepo.save(updatedWorker));
    }

    @Override
    public void delete(Long workerId) {
        getById(workerId);
        workerRepo.deleteById(workerId);
    }

    @Override
    public List<WorkerResponse> getAll() {
        return workerRepo.findAll()
                .stream()
                .map(worker ->  workerMapper.toResponse(worker))
                .toList();
    }

    @Override
    public Worker getById(Long workerId) {
        return workerRepo.findById(workerId).orElseThrow(
                () -> new NoSuchElementException("There is no worker with id = " + workerId)
        );
    }


    @Override
    public WorkerResponse getResponseById(Long workerId) {
        return workerMapper.toResponse(getById(workerId));
    }


    @Override
    public List<WorkerResponse> getAllByBranchId(Long branchId) {
        return branchService.getById(branchId).getWorkers()
                .stream().map(worker ->  workerMapper.toResponse(worker))
                .collect(Collectors.toList());
    }
}
