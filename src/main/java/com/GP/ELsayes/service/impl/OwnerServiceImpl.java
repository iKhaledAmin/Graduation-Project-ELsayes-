package com.GP.ELsayes.service.impl;


import com.GP.ELsayes.model.dto.BranchResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.OwnerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.OwnerResponse;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
import com.GP.ELsayes.model.enums.permissions.OwnerPermission;
import com.GP.ELsayes.model.enums.roles.UserRole;
import com.GP.ELsayes.model.mapper.OwnerMapper;
import com.GP.ELsayes.repository.OwnerRepo;
import com.GP.ELsayes.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class OwnerServiceImpl implements OwnerService {

    private final OwnerMapper ownerMapper;
    private final OwnerRepo ownerRepo;


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
    public OwnerResponse add(OwnerRequest ownerRequest) {
        Owner owner = this.ownerMapper.toEntity(ownerRequest);
        owner.setUserRole(UserRole.OWNER);
        owner.setOwnerPermission(OwnerPermission.RESTRICTED);
        return this.ownerMapper.toResponse(this.ownerRepo.save(owner));
    }

    @Override
    public List<OwnerResponse> getAll() {
        return ownerRepo.findAll()
                .stream()
                .map(owner ->  ownerMapper.toResponse(owner))
                .toList();
    }

    @Override
    public OwnerResponse update(OwnerRequest ownerRequest, Long ownerId) {
        Owner existedOwner = this.getById(ownerId);
        existedOwner = this.ownerMapper.toEntity(ownerRequest);
        existedOwner.setId(ownerId);
        return this.ownerMapper.toResponse(ownerRepo.save(existedOwner));
    }

    @Override
    public void delete(Long ownerId) {
        getById(ownerId);
        ownerRepo.deleteById(ownerId);
    }

}
