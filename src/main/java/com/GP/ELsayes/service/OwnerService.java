package com.GP.ELsayes.service;


import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.OwnerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.OwnerResponse;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
import org.springframework.stereotype.Service;

@Service
public interface OwnerService extends UserService , CrudService<OwnerRequest , Owner , OwnerResponse, Long >{
    public Owner getByManagerId(Long managerId);
}
