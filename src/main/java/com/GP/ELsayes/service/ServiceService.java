package com.GP.ELsayes.service;

import com.GP.ELsayes.model.dto.ServiceRequest;
import com.GP.ELsayes.model.dto.ServiceResponse;
import com.GP.ELsayes.model.dto.relations.ServicesOfBranchesRequest;
import com.GP.ELsayes.model.dto.relations.ServicesOfBranchesResponse;
import com.GP.ELsayes.model.dto.relations.ServicesOfPackageRequest;
import com.GP.ELsayes.model.dto.relations.ServicesOfPackagesResponse;
import com.GP.ELsayes.model.entity.ServiceEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ServiceService extends CrudService<ServiceRequest , ServiceEntity , ServiceResponse ,Long> {
    public Optional<ServiceEntity> getByServiceIdAndBranchId(Long serviceId, Long branchId);
    boolean isExistInBranch(Long serviceId, Long branchId) ;
    boolean isAvailableInBranch(Long serviceId, Long branchId) ;



    public ServicesOfBranchesResponse addServiceToBranch(ServicesOfBranchesRequest servicesOfBranchesRequest);
    public ServicesOfBranchesResponse activateServiceInBranch(ServicesOfBranchesRequest servicesOfBranchesRequest);
    public ServicesOfBranchesResponse deactivateServiceInBranch(ServicesOfBranchesRequest servicesOfBranchesRequest);
    public List<ServiceEntity> getAllByBranchId(Long branchId);
    public List<ServiceResponse> getResponseAllByBranchId(Long branchId);
    List<ServiceEntity> getAllAvailableInBranch(Long branchId);



    public ServicesOfPackagesResponse addServiceToOffer(ServicesOfPackageRequest servicesOfPackageRequest);

    public List<ServiceEntity> getAllByPackageId(Long offerId);
    public List<ServiceResponse> getResponseAllByOfferId(Long offerId);

    public List<ServiceEntity> getAllByOrderId(Long orderId);


    ServiceResponse toResponse(ServiceEntity service);
}

