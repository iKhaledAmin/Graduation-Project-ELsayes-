package com.GP.ELsayes.service.impl;


import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.OwnerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.OwnerResponse;
import com.GP.ELsayes.model.entity.OwnersOfRestrictedOwners;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
import com.GP.ELsayes.model.enums.CrudType;
import com.GP.ELsayes.model.enums.permissions.OwnerPermission;
import com.GP.ELsayes.model.enums.roles.UserRole;
import com.GP.ELsayes.model.mapper.OwnerMapper;
import com.GP.ELsayes.repository.OwnerRepo;
import com.GP.ELsayes.repository.OwnersOfRestrictedOwnersRepo;
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
public class OwnerServiceImpl implements OwnerService {

    private final OwnerMapper ownerMapper;
    private final OwnerRepo ownerRepo;
    private final OwnersOfRestrictedOwnersRepo ownersOfRestrictedOwnersRepo;
    //private OwnersOfRestrictedOwners ownersOfRestrictedOwners = new OwnersOfRestrictedOwners();

    @Override
    public OwnerResponse add(OwnerRequest ownerRequest) {
        Owner newOwner = this.ownerMapper.toEntity(ownerRequest);
        newOwner.setUserRole(UserRole.OWNER);
        newOwner.setOwnerPermission(OwnerPermission.RESTRICTED);
        newOwner = this.ownerRepo.save(newOwner);

        // this if condition must be removed at production (important note)
        if(ownerRequest.getOldOwnerId() != null){
            OwnersOfRestrictedOwners ownersOfRestrictedOwners = new OwnersOfRestrictedOwners();
            Owner oldOwner = this.getById(ownerRequest.getOldOwnerId());
            ownersOfRestrictedOwners.setRestrictedOwner(newOwner);
            ownersOfRestrictedOwners.setOldOwner(oldOwner);
            ownersOfRestrictedOwners.setOperationDate(new Date());
            ownersOfRestrictedOwners.setOperationType(CrudType.CREATE);
            ownersOfRestrictedOwnersRepo.save(ownersOfRestrictedOwners);
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
        updatedOwner.setOwnerPermission(existedOwner.getOwnerPermission());


        BeanUtils.copyProperties(existedOwner,updatedOwner);
        updatedOwner = ownerRepo.save(existedOwner);

        // this if condition must be removed at production (important note)
        if(ownerRequest.getOldOwnerId() != null){
            OwnersOfRestrictedOwners ownersOfRestrictedOwners = new OwnersOfRestrictedOwners();
            Owner oldOwner = this.getById(ownerRequest.getOldOwnerId());
            ownersOfRestrictedOwners.setRestrictedOwner(updatedOwner);
            ownersOfRestrictedOwners.setOldOwner(oldOwner);
            ownersOfRestrictedOwners.setOperationDate(new Date());
            ownersOfRestrictedOwners.setOperationType(CrudType.UPDATE);
            ownersOfRestrictedOwnersRepo.save(ownersOfRestrictedOwners);
        }

        return this.ownerMapper.toResponse(updatedOwner);
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





}
