package com.GP.ELsayes.service;

import com.GP.ELsayes.model.dto.ServiceRequest;
import com.GP.ELsayes.model.dto.ServiceResponse;
import com.GP.ELsayes.model.dto.ServicesOfBranchesRequest;
import com.GP.ELsayes.model.dto.ServicesOfBranchesResponse;
import com.GP.ELsayes.model.entity.ServiceEntity;
import org.springframework.stereotype.Service;

@Service
public interface ServiceService extends CrudService<ServiceRequest , ServiceEntity , ServiceResponse ,Long> {
    public ServicesOfBranchesResponse addServiceToBranch(ServicesOfBranchesRequest servicesOfBranchesRequest);
    public ServicesOfBranchesResponse activateServiceInBranch(ServicesOfBranchesRequest servicesOfBranchesRequest);
    public ServicesOfBranchesResponse deactivateServiceInBranch(ServicesOfBranchesRequest servicesOfBranchesRequest);
}
