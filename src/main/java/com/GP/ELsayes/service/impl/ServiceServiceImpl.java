package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.PackageRequest;
import com.GP.ELsayes.model.dto.ServiceRequest;
import com.GP.ELsayes.model.dto.ServiceResponse;
import com.GP.ELsayes.model.dto.relations.ServicesOfBranchesRequest;
import com.GP.ELsayes.model.dto.relations.ServicesOfBranchesResponse;
import com.GP.ELsayes.model.dto.relations.ServicesOfPackageRequest;
import com.GP.ELsayes.model.dto.relations.ServicesOfPackagesResponse;
import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.Package;
import com.GP.ELsayes.model.entity.relations.ManagersOfServices;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.enums.OperationType;
import com.GP.ELsayes.model.enums.ServiceCategory;
import com.GP.ELsayes.model.enums.Status;
import com.GP.ELsayes.model.mapper.ServiceMapper;
import com.GP.ELsayes.repository.ServiceRepo;
import com.GP.ELsayes.service.*;
import com.GP.ELsayes.service.relations.ManagersOfServicesService;
import com.GP.ELsayes.service.relations.ServicesOfBranchesService;
import com.GP.ELsayes.service.relations.ServicesOfPackagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;




@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {
    private final ServiceRepo serviceRepo;
    private  final ServiceMapper serviceMapper;
    private final ManagerService managerService;
    private  final PackageService packageService;
    private  final BranchService branchService;
    private  final OrderService orderService;

    private  final ManagersOfServicesService managersOfServicesService;
    private  final ServicesOfBranchesService servicesOfBranchesService;
    private  final ServicesOfPackagesService servicesOfpackagesService;




    void throwExceptionIfServiceStillIncludedInPackage(Long serviceId){
        List<Package> aPackages = packageService.getAllByServiceId(serviceId);
        if(aPackages.isEmpty())
            return;
        throw new RuntimeException("This service with id = "+ serviceId +" still included in offer, you can not delete it");

    }

    void throwExceptionIfServiceStillIncludedInBranch(Long serviceId){
        List<Branch> branches= branchService.getAllByServiceId(serviceId);
        if(branches.isEmpty())
            return;
        throw new RuntimeException("This service with id = "+ serviceId +" still included in branch, you can not delete it");

    }




    @Override
    public ServiceResponse add(ServiceRequest serviceRequest) {
        ServiceEntity service = this.serviceMapper.toEntity(serviceRequest);
        service = this.serviceRepo.save(service);

        Manager manager = managerService.getById(serviceRequest.getManagerId());
        ManagersOfServices managersOfServices= this.managersOfServicesService.add(
                manager,
                service,
                OperationType.CREATE
        );

        return this.serviceMapper.toResponse(service);
    }

    @Override
    public ServiceResponse update(ServiceRequest serviceRequest, Long serviceId) {

        ServiceEntity existedService = this.getById(serviceId);
        ServiceEntity updatedService = this.serviceMapper.toEntity(serviceRequest);

        updatedService.setId(serviceId);
        updatedService.setName(serviceRequest.getName());
        updatedService.setDescription(serviceRequest.getDescription());
        updatedService.setImage(serviceRequest.getImage());
        updatedService.setPrice(serviceRequest.getPrice());
        updatedService.setRequiredTime(serviceRequest.getRequiredTime());
        updatedService.setServiceCategory(serviceRequest.getServiceCategory());

        //BeanUtils.copyProperties(updatedService,existedService);
        updatedService = serviceRepo.save(updatedService);

        Manager manager = managerService.getById(serviceRequest.getManagerId());
        ManagersOfServices managersOfServices= this.managersOfServicesService.add(
                manager,
                updatedService,
                OperationType.UPDATE
        );

        return this.serviceMapper.toResponse(updatedService);
    }

    @Override
    public void delete(Long serviceId) {
        throwExceptionIfServiceStillIncludedInBranch(serviceId);
        throwExceptionIfServiceStillIncludedInPackage(serviceId);

        this.getById(serviceId);
        serviceRepo.deleteById(serviceId);
    }

    @Override
    public List<ServiceResponse> getAll() {
        return serviceRepo.findAll()
                .stream()
                .map(service ->  serviceMapper.toResponse(service))
                .toList();
    }

    @Override
    public Optional<ServiceEntity> getObjectById(Long serviceId) {
        return serviceRepo.findById(serviceId);
    }

    @Override
    public Optional<ServiceEntity> getByObjectByIdAndBranchId(Long serviceId, Long branchId) {
        return serviceRepo.findByServiceIdAndBranchId(serviceId, branchId);
    }

    @Override
    public ServiceEntity getByIdAndBranchId(Long serviceId,Long branchId) {
        return getObjectById(serviceId).orElseThrow(
                () -> new NoSuchElementException("There is no service with id = " + serviceId + " in this branch")
        );
    }

    @Override
    public ServiceEntity getById(Long serviceId) {
        return getObjectById(serviceId).orElseThrow(
                () -> new NoSuchElementException("There is no service with id = " + serviceId)
        );
    }

    @Override
    public ServiceResponse getResponseById(Long serviceId) {
        return serviceMapper.toResponse(getById(serviceId));
    }
    @Override
    public ServiceResponse toResponseAccordingToBranch(Long serviceId, Long branchId) {
        return serviceMapper.toResponseAccordingToBranch(getByIdAndBranchId(serviceId,branchId),branchId,servicesOfBranchesService);
    }



    @Override
    public ServiceResponse getByServiceIdOrByServiceIdAndBranchId(Long serviceId, Long branchId){
        if (branchId == null)
            return getResponseById(serviceId);
        else return toResponseAccordingToBranch(serviceId,branchId);
    }

    @Override
    public List<ServiceEntity> getAllByBranchId(Long branchId) {
        branchService.getById(branchId);
        return serviceRepo.findAllByBranchId(branchId);
    }

    @Override
    public List<ServiceResponse> getResponseAllByBranchId(Long branchId) {
        branchService.getById(branchId);
        return serviceRepo.findAllByBranchId(branchId)
                .stream()
                .map(service -> {
                    return serviceMapper.toResponseAccordingToBranch(service,branchId,servicesOfBranchesService);
                })
                .toList();
    }



    public List<ServiceEntity> getAllAvailableInBranch(Long branchId) {
        branchService.getById(branchId);
        return serviceRepo.findAllAvailableInBranch(branchId);
    }





    @Override
    public boolean isExistInBranch(Long serviceId, Long branchId) {
        Optional<ServiceEntity> serviceEntity = serviceRepo.findByServiceIdAndBranchId(serviceId, branchId);
        if(serviceEntity.isEmpty()){
            return false;
        }
        return true;
    }

    @Override
    public boolean isAvailableInBranch(Long serviceId, Long branchId) {
        Optional<ServiceEntity> serviceEntity = serviceRepo.findByServiceIdAndBranchIdIfAvailable(serviceId, branchId);
        if(serviceEntity.isEmpty()){
            return false;
        }
        return true;
    }


    @Override
    public ServicesOfBranchesResponse addServiceToBranch(ServicesOfBranchesRequest servicesOfBranchesRequest) {
        return servicesOfBranchesService.addServiceToBranch(
                servicesOfBranchesRequest.getServiceId(),
                servicesOfBranchesRequest.getBranchId()
        );
    }

    @Override
    public ServicesOfBranchesResponse activateServiceInBranch(ServicesOfBranchesRequest servicesOfBranchesRequest) {
        ServicesOfBranchesResponse servicesOfBranchesResponse =  servicesOfBranchesService.activateServiceInBranch(
                servicesOfBranchesRequest.getServiceId(),
                servicesOfBranchesRequest.getBranchId()
        );
        boolean isAvailable = isAvailableInBranch(  servicesOfBranchesRequest.getServiceId(), servicesOfBranchesRequest.getBranchId());
        servicesOfBranchesResponse.setServiceStatus(isAvailable ? Status.AVAILABLE : Status.UNAVAILABLE);

         return servicesOfBranchesResponse;
    }

    @Override
    public ServicesOfBranchesResponse deactivateServiceInBranch(ServicesOfBranchesRequest servicesOfBranchesRequest) {
        ServicesOfBranchesResponse servicesOfBranchesResponse =  servicesOfBranchesService.deactivateServiceInBranch(
                servicesOfBranchesRequest.getServiceId(),
                servicesOfBranchesRequest.getBranchId()
        );
        boolean isAvailable = isAvailableInBranch(  servicesOfBranchesRequest.getServiceId(), servicesOfBranchesRequest.getBranchId());
        servicesOfBranchesResponse.setServiceStatus(isAvailable ? Status.AVAILABLE : Status.UNAVAILABLE);

        return servicesOfBranchesResponse;
    }



    @Override
    public ServicesOfPackagesResponse addServiceToPackage(ServicesOfPackageRequest servicesOfPackageRequest) {

        Package aPackage = packageService.getById(servicesOfPackageRequest.getPackageId());

        ServicesOfPackagesResponse servicesOfPackagesResponse = servicesOfpackagesService.addServiceToPackage(
                servicesOfPackageRequest.getServiceId(),
                servicesOfPackageRequest.getPackageId()
        );

        PackageRequest packageRequest = new PackageRequest();
        packageRequest.setPercentageOfDiscount(aPackage.getPercentageOfDiscount());
        packageRequest.setManagerId(managerService.getByOfferId(aPackage.getId()).getId());
        packageService.update(packageRequest, aPackage.getId());

        return servicesOfPackagesResponse;
    }

    @Override
    public List<ServiceEntity> getAllByPackageId(Long offerId) {
        Package aPackage = packageService.getById(offerId);
        return serviceRepo.findAllByPackageId(offerId);
    }

    @Override
    public List<ServiceResponse> getResponseAllByPackageId(Long packageId
    ) {
        Package aPackage = packageService.getById(packageId);
        return serviceRepo.findAllByPackageId(packageId)
                .stream()
                .map(service ->  serviceMapper.toResponse(service))
                .toList();
    }

    @Override
    public List<ServiceEntity> getAllByOrderId(Long orderId) {
        return serviceRepo.findAllByOrderId(orderId);
    }

    @Override
    public ServiceResponse toResponse(ServiceEntity service) {
        return serviceMapper.toResponse(service);
    }

    @Override
    public List<ServiceResponse> getAllByCategory(ServiceCategory category) {
        return serviceRepo.findAllByCategory(category)
                .stream()
                .map(service -> serviceMapper.toResponse(service))
                .toList();
    }

    @Override
    public List<ServiceResponse> getAllCleaningServices(){
        return getAllByCategory(ServiceCategory.CLEANING_SERVICE);
    }

    @Override
    public List<ServiceResponse> getAllMaintenanceServices(){
        return getAllByCategory(ServiceCategory.MAINTENANCE_SERVICE);
    }

    @Override
    public List<ServiceResponse> getAllTakeAwayServices(){
        return getAllByCategory(ServiceCategory.TAKE_AWAY_SERVICE);
    }
}
