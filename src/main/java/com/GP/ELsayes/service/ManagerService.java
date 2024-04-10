package com.GP.ELsayes.service;

import com.GP.ELsayes.model.dto.PackageRequest;
import com.GP.ELsayes.model.dto.PackageResponse;
import com.GP.ELsayes.model.dto.ServiceRequest;
import com.GP.ELsayes.model.dto.ServiceResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.ManagerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.ManagerResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.WorkerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.WorkerResponse;
import com.GP.ELsayes.model.dto.relations.*;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ManagerService extends UserService , CrudService<ManagerRequest, Manager, ManagerResponse,Long>{

    public Manager getByBranchId(long branchId);
    public Manager getByOfferId(long offerId);
    public ManagerResponse getResponseByBranchId(Long branchId);
    public Optional<Manager> getIfExistByBranchId(Long managerId);


    public WorkerResponse addWorker(WorkerRequest workerRequest);
    public WorkerResponse updateWorker(WorkerRequest workerRequest, Long workerId);
    public void deleteWorker(Long workerId);
    public List<WorkerResponse> getAllWorkers();
    public WorkerResponse getWorkerResponseById(Long workerId);
    public List<WorkerResponse> getAllWorkersByBranchId(Long branchId);



    public ServiceResponse addService(ServiceRequest serviceRequest);
    public ServiceResponse updateService(ServiceRequest serviceRequest, Long serviceId);
    public void deleteService(Long serviceId);
    public List<ServiceResponse> getAllServices();
    public ServiceResponse getServiceResponseById(Long serviceId);
    public ServicesOfBranchesResponse addServiceToBranch(ServicesOfBranchesRequest servicesOfBranchesRequest);
    public ServicesOfBranchesResponse activateServiceInBranch(ServicesOfBranchesRequest servicesOfBranchesRequest);
    public ServicesOfBranchesResponse deactivateServiceInBranch(ServicesOfBranchesRequest servicesOfBranchesRequest);
    public ServicesOfPackagesResponse addServiceToPackage(ServicesOfPackageRequest servicesOfPackageRequest);
    public List<ServiceEntity> getAllServicesByBranchId(Long branchId);



    public PackageResponse addPackage(PackageRequest packageRequest);
    public PackageResponse updatePackage(PackageRequest packageRequest, Long offerId);
    public void deletePackage(Long offerId);
    public List<PackageResponse> getAllPackages();
    public PackageResponse getPackageResponseById(Long offerId);
    public PackageOfBranchesResponse addPackageToBranch(PackageOfBranchesRequest packageOfBranchesRequest);
    public PackageOfBranchesResponse activatePackageInBranch(PackageOfBranchesRequest packageOfBranchesRequest);
    public PackageOfBranchesResponse deactivatePackageInBranch(PackageOfBranchesRequest packageOfBranchesRequest);
    public List<PackageResponse> getAllPackageResponseByBranchId(Long branchId);


}
