package com.GP.ELsayes.service.impl;


import com.GP.ELsayes.model.dto.BranchRequest;
import com.GP.ELsayes.model.dto.BranchResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.EditUserProfileRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.ManagerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.ManagerResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.OwnerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.OwnerResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserResponse;
import com.GP.ELsayes.model.dto.relations.VisitationsOfBranchesResponse;
import com.GP.ELsayes.model.entity.relations.OwnersOfRestrictedOwners;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
import com.GP.ELsayes.model.enums.OperationType;
import com.GP.ELsayes.model.enums.permissions.OwnerPermission;
import com.GP.ELsayes.model.enums.roles.UserRole;
import com.GP.ELsayes.model.mapper.OwnerMapper;
import com.GP.ELsayes.model.mapper.UserMapper;
import com.GP.ELsayes.repository.OwnerRepo;
import com.GP.ELsayes.service.BranchService;
import com.GP.ELsayes.service.ManagerService;
import com.GP.ELsayes.service.OwnerService;
import com.GP.ELsayes.service.UserService;
import com.GP.ELsayes.service.relations.OwnersOfRestrictedOwnersService;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class OwnerServiceImpl
        implements  OwnerService {

    private final OwnerMapper ownerMapper;
    private final OwnerRepo ownerRepo;
    private final UserMapper userMapper;
    private final UserService userService;
    private final ManagerService managerService;
    private final BranchService branchService;
    private final OwnersOfRestrictedOwnersService ownersOfRestrictedOwnersService;

    public OwnerServiceImpl(OwnerMapper ownerMapper, OwnerRepo ownerRepo, UserMapper userMapper,
                            UserService userService, ManagerService managerService, @Lazy BranchService branchService,
                            OwnersOfRestrictedOwnersService ownersOfRestrictedOwnersService) {
        this.ownerMapper = ownerMapper;
        this.ownerRepo = ownerRepo;
        this.userMapper = userMapper;
        this.userService = userService;
        this.managerService = managerService;
        this.branchService = branchService;
        this.ownersOfRestrictedOwnersService = ownersOfRestrictedOwnersService;
    }

    private void throwExceptionIfThereIsAMainOwnerAndOldOwnerIdISNULL(Long oldOwnerId) {
        Optional<OwnersOfRestrictedOwners> mainOwner = ownersOfRestrictedOwnersService.findTneMainOwner();
        if(oldOwnerId == null && mainOwner.isPresent())
            throw new RuntimeException("Please, enter the old owner");
        if (oldOwnerId != null)
            getById(oldOwnerId);
    }
    public Boolean mainOwnerIsExist(){
        Optional<OwnersOfRestrictedOwners> mainOwner = ownersOfRestrictedOwnersService.findTneMainOwner();
        if(mainOwner.isPresent())
            return true;
        else return false;
    }

    private void throwExceptionIfThereIsAMainOwner() {
        Optional<OwnersOfRestrictedOwners> mainOwner = ownersOfRestrictedOwnersService.findTneMainOwner();
        if(mainOwner.isPresent())
            throw new RuntimeException("The main owner already registered,can not register new one");
    }

    private void throwExceptionIfUserNameAlreadyExist(String userName) {
        Optional<Owner> owner = ownerRepo.findByUserName(userName);
        if(owner.isPresent())
            throw new RuntimeException("User name already exist");
    }



    @Override
    public OwnerResponse add(OwnerRequest ownerRequest) {
        throwExceptionIfThereIsAMainOwnerAndOldOwnerIdISNULL(ownerRequest.getOldOwnerId());
        throwExceptionIfUserNameAlreadyExist(ownerRequest.getUserName());


        Owner newOwner = this.ownerMapper.toEntity(ownerRequest);
//        newOwner.setOwnerPermission(ownerRequest.getOwnerPermission());
//        if (ownerRequest.getOwnerPermission() == OwnerPermission.FULL_PERMISSION)
//            newOwner.setUserRole(UserRole.TOP_OWNER);
//        else newOwner.setUserRole(UserRole.OWNER);
        newOwner.setUserRole(UserRole.OWNER);
        newOwner = this.ownerRepo.save(newOwner);

        Owner oldOwner = null;
        if (ownerRequest.getOldOwnerId() != null){
             oldOwner = this.getById(ownerRequest.getOldOwnerId());
        }

        OwnersOfRestrictedOwners ownersOfRestrictedOwners = ownersOfRestrictedOwnersService.add(
                oldOwner,
                newOwner,
                OperationType.CREATE
        );


        return this.ownerMapper.toResponse(newOwner);
    }

    @Override
    public OwnerResponse register(UserRequest userRequest){
        throwExceptionIfThereIsAMainOwner();
        OwnerRequest ownerRequest = userMapper.toOwnerRequest(userRequest);
        //ownerRequest.setOwnerPermission(OwnerPermission.FULL_PERMISSION);
        return add(ownerRequest);

    }



    @SneakyThrows
    @Override
    public OwnerResponse update(OwnerRequest ownerRequest, Long ownerId) {

        Owner existedOwner = this.getById(ownerId);

        Owner updatedOwner = this.ownerMapper.toEntity(ownerRequest);
        updatedOwner.setId(ownerId);
        updatedOwner.setUserName(existedOwner.getUserName());
//        updatedOwner.setOwnerPermission(ownerRequest.getOwnerPermission());
//        if (ownerRequest.getOwnerPermission() == OwnerPermission.FULL_PERMISSION)
//            updatedOwner.setUserRole(UserRole.TOP_OWNER);
//        else updatedOwner.setUserRole(UserRole.OWNER);
        updatedOwner.setUserRole(UserRole.OWNER);
        BeanUtils.copyProperties(existedOwner,updatedOwner);

        updatedOwner = ownerRepo.save(existedOwner);

        // this if condition must be removed at production (important note)
        if(ownerRequest.getOldOwnerId() != null){
            Owner oldOwner = this.getById(ownerRequest.getOldOwnerId());
            OwnersOfRestrictedOwners ownersOfRestrictedOwners = ownersOfRestrictedOwnersService.add(
                    oldOwner,
                    updatedOwner,
                    OperationType.UPDATE
            );
        }

        return this.ownerMapper.toResponse(updatedOwner);
    }


    @Override
    public UserResponse editProfile(EditUserProfileRequest profileRequest, Long userId) {
        getById(userId);
        return userService.editProfile(profileRequest,userId);
    }

    @Override
    public void delete(Long ownerId) {
        getById(ownerId);
        ownerRepo.deleteById(ownerId);
    }

    @Override
    public List<OwnerResponse> getAll() {
        return ownerRepo.findAll()
                .stream()
                .map(owner ->  ownerMapper.toResponse(owner))
                .toList();
    }

    @Override
    public Optional<Owner> getObjectById(Long ownerId) {
        return ownerRepo.findById(ownerId);
    }


    @Override
    public Owner getById(Long ownerId) {
        return getObjectById(ownerId).orElseThrow(
                () -> new NoSuchElementException("There Are No Owner With Id = " + ownerId)
        );
    }

    @Override
    public OwnerResponse getResponseById(Long ownerId) {
        return ownerMapper.toResponse(getById(ownerId));
    }


    @Override
    public Owner getByManagerId(Long managerId) {
        return ownerRepo.findByManagerId(managerId).orElseThrow(
                () -> new NoSuchElementException("There is no owner whit manager id = " + managerId)
        );
    }



    @Override
    public ManagerResponse addManager(ManagerRequest managerRequest){
        return managerService.add(managerRequest);
    }
    @Override
    public ManagerResponse updateManager(ManagerRequest managerRequest, Long managerId){
        return managerService.update(managerRequest,managerId);
    }
    @Override
    public void deleteManager(Long managerId){
         managerService.delete(managerId);
    }
    @Override
    public List<ManagerResponse> getAllManager(){
        return managerService.getAll();
    }
    @Override
    public ManagerResponse getResponseManagerById(Long managerId){
        return managerService.getResponseById(managerId);
    }
    @Override
    public ManagerResponse getResponseManagerByBranchId(Long branchId){
        return managerService.getResponseByBranchId(branchId);
    }







    @Override
    public BranchResponse addBranch(BranchRequest branchRequest){
        return branchService.add(branchRequest);
    }
    @Override
    public BranchResponse updateBranch(BranchRequest branchRequest, Long branchId){
        return branchService.update(branchRequest,branchId);
    }
    @Override
    public void deleteBranch(Long branchId){
         branchService.delete(branchId);
    }
    @Override
    public List<BranchResponse> getAllBranches(){
        return branchService.getAll();
    }
    @Override
    public BranchResponse getBranchResponseById(Long branchId){
        return branchService.getResponseById(branchId);
    }
    @Override
    public List<VisitationsOfBranchesResponse> getResponseAllCurrentVisitationsInBranch(Long branchId) {
        return branchService.getResponseAllCurrentVisitationsInBranch(branchId);
    }
    @Override
    public List<VisitationsOfBranchesResponse> getResponseAllVisitationsInBranchByADate(Long branchId, Date date) {
        return branchService.getResponseAllVisitationsInBranchByADate(branchId,date);
    }

}
