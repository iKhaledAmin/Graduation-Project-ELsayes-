package com.GP.ELsayes.service.impl;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.ManagerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.ManagerResponse;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.enums.roles.UserRole;
import com.GP.ELsayes.model.mapper.ManagerMapper;
import com.GP.ELsayes.repository.ManagerRepo;
import com.GP.ELsayes.service.BranchService;
import com.GP.ELsayes.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class ManagerServiceImpl implements ManagerService {
    private final ManagerMapper managerMapper;
    private final ManagerRepo managerRepo;
    private final BranchService branchService;


    @Override
    public Manager getById(Long managerId) {
        return managerRepo.findById(managerId).orElseThrow(
                () -> new NoSuchElementException("There Is No Manager With Id = " + managerId)
        );
    }

    @Override
    public Manager getByBranchId(long branchId) {
        return managerRepo.findByBranchId(branchId).orElseThrow(
                () -> new NoSuchElementException("There Is No Manager With Branch Id = " + branchId)
        );
    }

    @Override
    public ManagerResponse add(ManagerRequest managerRequest) {
        Manager manager = this.managerMapper.toEntity(managerRequest);

        Optional<Manager> branchManager = this.managerRepo.findByBranchId(managerRequest.getManagedBranchId());
        if(branchManager.isPresent()){
            throw new RuntimeException("This branch already have manager and every branch only have one manager");
        }

        manager.setManagedBranch(branchService.getById(managerRequest.getManagedBranchId()));
        manager.setUserRole(UserRole.MANAGER);
        return this.managerMapper.toResponse(this.managerRepo.save(manager));

    }

    @Override
    public List<ManagerResponse> getAll() {
        return managerRepo.findAll()
                .stream()
                .map(manager ->  managerMapper.toResponse(manager))
                .toList();
    }

    @Override
    public ManagerResponse update(ManagerRequest managerRequest, Long managerId) {
        Manager existedManager = this.getById(managerId);
        existedManager = this.managerMapper.toEntity(managerRequest);
        existedManager.setId(managerId);
        return this.managerMapper.toResponse(managerRepo.save(existedManager));
    }

    @Override
    public void delete(Long managerId) {
        getById(managerId);
        managerRepo.deleteById(managerId);
    }


}
