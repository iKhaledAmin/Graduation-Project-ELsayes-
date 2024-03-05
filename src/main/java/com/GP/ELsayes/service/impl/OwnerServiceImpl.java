package com.GP.ELsayes.service.impl;


import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.OwnerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.OwnerResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserResponse;
import com.GP.ELsayes.model.entity.relations.OwnersOfRestrictedOwners;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
import com.GP.ELsayes.model.enums.OperationType;
import com.GP.ELsayes.model.enums.permissions.OwnerPermission;
import com.GP.ELsayes.model.enums.roles.UserRole;
import com.GP.ELsayes.model.mapper.OwnerMapper;
import com.GP.ELsayes.model.mapper.UserMapper;
import com.GP.ELsayes.repository.OwnerRepo;
import com.GP.ELsayes.service.OwnerService;
import com.GP.ELsayes.service.UserService;
import com.GP.ELsayes.service.relations.OwnersOfRestrictedOwnersService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

import static java.lang.Double.parseDouble;

@RequiredArgsConstructor
@Service
public class OwnerServiceImpl
        implements UserService , OwnerService {

    private final OwnerMapper ownerMapper;
    private final OwnerRepo ownerRepo;
    private final UserMapper userMapper;
    private final OwnersOfRestrictedOwnersService ownersOfRestrictedOwnersService;


    private OwnerPermission setPermission(String ownerPercentage){
        if (parseDouble(ownerPercentage) > 60.0)
            return OwnerPermission.FULL_PERMISSION;
        else
            return OwnerPermission.RESTRICTED;
    }

    @Override
    public OwnerResponse add(OwnerRequest ownerRequest) {
        Owner newOwner = this.ownerMapper.toEntity(ownerRequest);
        newOwner.setUserRole(UserRole.OWNER);
        newOwner.setOwnerPermission(setPermission(ownerRequest.getPercentage()));
        newOwner = this.ownerRepo.save(newOwner);

        // this if condition must be removed at production (important note)
        if(ownerRequest.getOldOwnerId() != null){
            Owner oldOwner = this.getById(ownerRequest.getOldOwnerId());
            OwnersOfRestrictedOwners ownersOfRestrictedOwners = ownersOfRestrictedOwnersService.save(
                    oldOwner,
                    newOwner,
                    OperationType.CREATE
            );

        }

        return this.ownerMapper.toResponse(newOwner);
    }

    @SneakyThrows
    @Override
    public OwnerResponse update(OwnerRequest ownerRequest, Long ownerId) {

        Owner existedOwner = this.getById(ownerId);

        Owner updatedOwner = this.ownerMapper.toEntity(ownerRequest);
        updatedOwner.setId(ownerId);
        updatedOwner.setUserRole(existedOwner.getUserRole());
        updatedOwner.setOwnerPermission(setPermission(ownerRequest.getPercentage()));

        BeanUtils.copyProperties(existedOwner,updatedOwner);

        updatedOwner = ownerRepo.save(existedOwner);

        // this if condition must be removed at production (important note)
        if(ownerRequest.getOldOwnerId() != null){
            Owner oldOwner = this.getById(ownerRequest.getOldOwnerId());
            OwnersOfRestrictedOwners ownersOfRestrictedOwners = ownersOfRestrictedOwnersService.save(
                    oldOwner,
                    updatedOwner,
                    OperationType.UPDATE
            );
        }

        return this.ownerMapper.toResponse(updatedOwner);
    }

    @Override
    public UserResponse editProfile(UserRequest userRequest, Long userId) {
        Owner owner = getById(userId);

        OwnerRequest ownerRequest = userMapper.toOwnerRequest(userRequest);
        ownerRequest.setPercentage(owner.getPercentage());


        OwnerResponse ownerResponse = update(ownerRequest,userId);

        return userMapper.toUserResponse(ownerResponse);
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
    public Owner getById(Long ownerId) {
        return ownerRepo.findById(ownerId).orElseThrow(
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
}
