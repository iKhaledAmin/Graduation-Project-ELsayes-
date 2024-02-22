package com.GP.ELsayes.service.impl;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.ManagerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.ManagerResponse;
import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.OwnersOfManagers;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
import com.GP.ELsayes.model.enums.CrudType;
import com.GP.ELsayes.model.enums.roles.UserRole;
import com.GP.ELsayes.model.mapper.ManagerMapper;
import com.GP.ELsayes.repository.ManagerRepo;
import com.GP.ELsayes.repository.OwnersOfManagersRepo;
import com.GP.ELsayes.service.BranchService;
import com.GP.ELsayes.service.ManagerService;
import com.GP.ELsayes.service.OwnerService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;


@RequiredArgsConstructor
@Service
public class ManagerServiceImpl implements ManagerService {
    private final ManagerMapper managerMapper;
    private final ManagerRepo managerRepo;
    private final BranchService branchService;
    private final OwnerService ownerService;
    private final OwnersOfManagersRepo ownersOfManagersRepo;
    //private final OwnersOfManagers ownersOfManagers = new OwnersOfManagers();

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

        Branch branch = branchService.getById(managerRequest.getBranchId());
        throwExceptionIfBranchAlreadyHasAManager(branch);

        manager = this.managerRepo.save(manager);


        Owner owner = ownerService.getById(managerRequest.getOwnerId());
        OwnersOfManagers ownersOfManagers = new OwnersOfManagers();
        ownersOfManagers.setOwner(owner);
        ownersOfManagers.setManager(manager);
        ownersOfManagers.setOperationDate(new Date());
        ownersOfManagers.setOperationType(CrudType.CREATE);
        ownersOfManagersRepo.save(ownersOfManagers);


        return this.managerMapper.toResponse(manager);
    }


    @SneakyThrows
    @Override
    public ManagerResponse update(ManagerRequest managerRequest, Long managerId) {

        Manager existedManager = this.getById(managerId);
        Manager updatedManager = this.managerMapper.toEntity(managerRequest);


        updatedManager.setId(managerId);
        updatedManager.setDateOfEmployment(existedManager.getDateOfEmployment());
        updatedManager.setUserRole(existedManager.getUserRole());


        BeanUtils.copyProperties(existedManager,updatedManager);

        Branch branch = branchService.getById(managerRequest.getBranchId());
        throwExceptionIfBranchHasAdifferentManager(branch,managerId);
        updatedManager.setBranch(branch);
        updatedManager = managerRepo.save(updatedManager);

        Owner owner = ownerService.getById(managerRequest.getOwnerId());
        OwnersOfManagers ownersOfManagers = new OwnersOfManagers();
        ownersOfManagers.setOwner(owner);
        ownersOfManagers.setManager(updatedManager);
        ownersOfManagers.setOperationDate(new Date());
        ownersOfManagers.setOperationType(CrudType.UPDATE);
        ownersOfManagersRepo.save(ownersOfManagers);


        return this.managerMapper.toResponse(updatedManager);
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
    public Manager getByBranchId(long branchId) {
        return managerRepo.findByBranchId(branchId).orElseThrow(
                () -> new NoSuchElementException("There is no manager with branch id = " + branchId)
        );
    }

    @Override
    public ManagerResponse getResponseById(Long managerId) {
        return managerMapper.toResponse(getById(managerId));
    }

    public ManagerResponse getResponseByBranchId(Long branchId) {
        return managerMapper.toResponse(getByBranchId(branchId));
    }


}
