package com.GP.ELsayes.service;

import com.GP.ELsayes.model.dto.ServiceRequest;
import com.GP.ELsayes.model.dto.ServiceResponse;
import com.GP.ELsayes.model.dto.relations.ServicesOfBranchesRequest;
import com.GP.ELsayes.model.dto.relations.ServicesOfBranchesResponse;
import com.GP.ELsayes.model.dto.relations.ServicesOfPackageRequest;
import com.GP.ELsayes.model.dto.relations.ServicesOfPackagesResponse;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.enums.ServiceCategory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ServiceService extends CrudService<ServiceRequest , ServiceEntity , ServiceResponse ,Long> {
    public Optional<ServiceEntity> getByObjectByIdAndBranchId(Long serviceId, Long branchId);
    public ServiceEntity getByIdAndBranchId(Long serviceId,Long branchId);
    public ServiceResponse getResponseByIdAndBranchId(Long serviceId, Long branchId);
    public ServiceResponse getResponseByServiceIdOrAndServiceIdBranchId(Long serviceId, Long branchId);
    boolean isExistInBranch(Long serviceId, Long branchId) ;
    boolean isAvailableInBranch(Long serviceId, Long branchId) ;



    public ServicesOfBranchesResponse addServiceToBranch(ServicesOfBranchesRequest servicesOfBranchesRequest);
    public ServicesOfBranchesResponse activateServiceInBranch(ServicesOfBranchesRequest servicesOfBranchesRequest);
    public ServicesOfBranchesResponse deactivateServiceInBranch(ServicesOfBranchesRequest servicesOfBranchesRequest);
    public List<ServiceEntity> getAllByBranchId(Long branchId);
  //  public List<ServiceResponse> getResponseAllByBranchId(Long branchId);
    List<ServiceEntity> getAllAvailableInBranch(Long branchId);



    public ServicesOfPackagesResponse addServiceToPackage(ServicesOfPackageRequest servicesOfPackageRequest);

    public List<ServiceEntity> getAllByPackageId(Long offerId);
    public List<ServiceResponse> getResponseAllByPackageId(Long offerId);

    public List<ServiceEntity> getAllByOrderId(Long orderId);


    ServiceResponse toResponse(ServiceEntity service);

    public List<ServiceResponse> getAllByCategory(ServiceCategory category);
    public List<ServiceResponse> getAllCleaningServices();
    public List<ServiceResponse> getAllMaintenanceServices();
    public List<ServiceResponse> getAllTakeAwayServices();
}

