package com.GP.ELsayes.service.impl;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.ManagerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.ManagerResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserResponse;
import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.relations.OwnersOfManagers;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
import com.GP.ELsayes.model.enums.OperationType;
import com.GP.ELsayes.model.enums.roles.UserRole;
import com.GP.ELsayes.model.mapper.ManagerMapper;
import com.GP.ELsayes.model.mapper.UserMapper;
import com.GP.ELsayes.repository.ManagerRepo;
import com.GP.ELsayes.service.BranchService;
import com.GP.ELsayes.service.ManagerService;
import com.GP.ELsayes.service.OwnerService;
import com.GP.ELsayes.service.UserService;
import com.GP.ELsayes.service.relations.OwnersOfManagersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class ManagerServiceImpl implements UserService, ManagerService {
    private final ManagerMapper managerMapper;
    private final ManagerRepo managerRepo;
    private final UserMapper userMapper;
    private final BranchService branchService;
    private final OwnerService ownerService;
    private final OwnersOfManagersService ownersOfManagersService;
;


    @Override
    public Optional<Manager> getIfExistByBranchId(Long managerId) {
        return managerRepo.findByBranchId(managerId);
    }

    void throwExceptionIfBranchAlreadyHasAManager(Branch branch){
        if(branch.getManager() == null)
            return;
        throw new RuntimeException("This branch with id = "+ branch.getId() +" already have a manager and every branch only have one manager");
    }

    void throwExceptionIfBranchHasAdifferentManager(Branch branch , Long managerId){

        if(branch.getManager() == null || branch.getManager().getId() == managerId)
            return;
        //throwExceptionIfBranchAlreadyHasAManager(branch);
        throw new RuntimeException("This branch with id = "+ branch.getId() +" already have a manager and every branch only have one manager");
    }


    @Override
    public ManagerResponse add(ManagerRequest managerRequest) {

        Manager manager = this.managerMapper.toEntity(managerRequest);
        manager.setUserRole(UserRole.MANAGER);
        manager.setBranch(branchService.getById(managerRequest.getBranchId()));
        manager.setDateOfEmployment(new Date());
        manager.setTotalSalary(emp -> {
            double baseSalary = Double.parseDouble(emp.getBaseSalary());
            double bonus = Double.parseDouble(emp.getBonus());
            return baseSalary + bonus;
        });


        Branch branch = branchService.getById(managerRequest.getBranchId());
        throwExceptionIfBranchAlreadyHasAManager(branch);

        manager = this.managerRepo.save(manager);


        Owner owner = ownerService.getById(managerRequest.getOwnerId());
        OwnersOfManagers ownersOfManagers = ownersOfManagersService.add(
                owner,
                manager,
                OperationType.CREATE
        );


        return this.managerMapper.toResponse(manager);
    }


    @Override
    public ManagerResponse update(ManagerRequest managerRequest, Long managerId) {
        Manager existedManager = this.getById(managerId);
        Manager updatedManager = this.managerMapper.toEntity(managerRequest);

        // Set fields from the existing manager that are not supposed to change
        updatedManager.setUserRole(existedManager.getUserRole());
        updatedManager.setDateOfEmployment(existedManager.getDateOfEmployment());


        if (updatedManager.getBaseSalary() == null || updatedManager.getBonus() == null) {
            updatedManager.setBaseSalary(existedManager.getBaseSalary());
            updatedManager.setBonus(existedManager.getBonus());
        }

        updatedManager.setId(managerId);
        // Check if the branch has a different manager before setting it
        Branch branch = branchService.getById(managerRequest.getBranchId());
        throwExceptionIfBranchHasAdifferentManager(branch, managerId);
        updatedManager.setBranch(branch);
        updatedManager.setTotalSalary(emp -> {
            double baseSalary = (emp.getBaseSalary() != null && !emp.getBaseSalary().trim().isEmpty())
                    ? Double.parseDouble(emp.getBaseSalary().trim()) : 0;
            double bonus = (emp.getBonus() != null && !emp.getBonus().trim().isEmpty())
                    ? Double.parseDouble(emp.getBonus().trim()) : 0;
            return baseSalary + bonus;
        });
        updatedManager = managerRepo.save(updatedManager);

        // Handle the ownership relationship
        Owner owner = ownerService.getById(managerRequest.getOwnerId());
        OwnersOfManagers ownersOfManagers = ownersOfManagersService.add(
                owner,
                updatedManager,
                OperationType.UPDATE
        );

        return this.managerMapper.toResponse(updatedManager);
    }

    @Override
    public UserResponse editProfile(UserRequest userRequest, Long userId) {
        Manager manager = getById(userId);
        Owner owner = ownerService.getByManagerId(manager.getId());

        ManagerRequest managerRequest = userMapper.toManagerRequest(userRequest);


        managerRequest.setManagerPermission(manager.getManagerPermission());
        managerRequest.setBranchId(manager.getBranch().getId());
        managerRequest.setOwnerId(owner.getId());
        System.out.println("TTTTTTTTEdit " + managerRequest.getTotalSalary());


        ManagerResponse managerResponse = update(managerRequest,userId);

        return userMapper.toUserResponse(managerResponse);
    }


    @Override
    public void delete(Long managerId) {
        this.getById(managerId);
        managerRepo.deleteById(managerId);
    }

    @Override
    public List<ManagerResponse> getAll() {
        return managerRepo.findAll()
                .stream()
                .map(manager ->  managerMapper.toResponse(manager))
                .toList();
    }

    @Override
    public Manager getById(Long managerId) {
        return managerRepo.findById(managerId).orElseThrow(
                () -> new NoSuchElementException("There is no manager with id = " + managerId)
        );
    }
    @Override
    public ManagerResponse getResponseById(Long managerId) {
        return managerMapper.toResponse(getById(managerId));
    }

    @Override
    public Manager getByBranchId(long branchId) {
        return managerRepo.findByBranchId(branchId).orElseThrow(
                () -> new NoSuchElementException("There is no manager with branch id = " + branchId)
        );
    }

    @Override
    public Manager getByOfferId(long offerId) {
        return managerRepo.findByOfferId(offerId).orElseThrow(
                () -> new NoSuchElementException("There is no manager for offer with id = " + offerId)
        );
    }

    public ManagerResponse getResponseByBranchId(Long branchId) {
        return managerMapper.toResponse(getByBranchId(branchId));
    }


}
