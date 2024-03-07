package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.OfferRequest;
import com.GP.ELsayes.model.dto.ServiceRequest;
import com.GP.ELsayes.model.dto.ServiceResponse;
import com.GP.ELsayes.model.dto.relations.ServicesOfBranchesRequest;
import com.GP.ELsayes.model.dto.relations.ServicesOfBranchesResponse;
import com.GP.ELsayes.model.dto.relations.ServicesOfOffersRequest;
import com.GP.ELsayes.model.dto.relations.ServicesOfOffersResponse;
import com.GP.ELsayes.model.entity.Offer;
import com.GP.ELsayes.model.entity.relations.ManagersOfServices;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.enums.OperationType;
import com.GP.ELsayes.model.mapper.ServiceMapper;
import com.GP.ELsayes.repository.ServiceRepo;
import com.GP.ELsayes.service.BranchService;
import com.GP.ELsayes.service.ManagerService;
import com.GP.ELsayes.service.OfferService;
import com.GP.ELsayes.service.ServiceService;
import com.GP.ELsayes.service.relations.ManagersOfServicesService;
import com.GP.ELsayes.service.relations.ServicesOfBranchesService;
import com.GP.ELsayes.service.relations.ServicesOfOffersService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;



@RequiredArgsConstructor
@Service
public class ServiceServiceImpl implements ServiceService {
    private final ServiceRepo serviceRepo;
    private final ServiceMapper serviceMapper;
    private final ManagerService managerService;
    private final OfferService offerService;
    private final BranchService branchService;
    private final ManagersOfServicesService managersOfServicesService;
    private final ServicesOfBranchesService servicesOfBranchesService;
    private final ServicesOfOffersService servicesOfOffersService;


    void throwExceptionIfServiceStillIncludedInOffer(Long serviceId){
        List<Offer> offers= offerService.getAllOffersIncludeService(serviceId);
        if(offers.isEmpty())
            return;
        throw new RuntimeException("This service with id = "+ serviceId +" still included in offer, you can not delete it");

    }


    @Override
    public ServiceResponse add(ServiceRequest serviceRequest) {
        ServiceEntity service = this.serviceMapper.toEntity(serviceRequest);
        service = this.serviceRepo.save(service);

        Manager manager = managerService.getById(serviceRequest.getManagerId());
        ManagersOfServices managersOfServices= this.managersOfServicesService.save(
                manager,
                service,
                OperationType.CREATE
        );

        return this.serviceMapper.toResponse(service);
    }

    @SneakyThrows
    @Override
    public ServiceResponse update(ServiceRequest serviceRequest, Long serviceId) {

        ServiceEntity existedService = this.getById(serviceId);
        ServiceEntity updatedService = this.serviceMapper.toEntity(serviceRequest);

        updatedService.setId(serviceId);
        BeanUtils.copyProperties(updatedService,existedService);
        updatedService = serviceRepo.save(existedService);

        Manager manager = managerService.getById(serviceRequest.getManagerId());
        ManagersOfServices managersOfServices= this.managersOfServicesService.save(
                manager,
                updatedService,
                OperationType.UPDATE
        );

        return this.serviceMapper.toResponse(updatedService);
    }

    @Override
    public void delete(Long serviceId) {
        throwExceptionIfServiceStillIncludedInOffer(serviceId);
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
    public ServiceEntity getById(Long serviceId) {
        return serviceRepo.findById(serviceId).orElseThrow(
                () -> new NoSuchElementException("There is no service with id = " + serviceId)
        );
    }


    @Override
    public ServiceResponse getResponseById(Long serviceId) {
        return serviceMapper.toResponse(getById(serviceId));
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
        return servicesOfBranchesService.activateServiceInBranch(
                servicesOfBranchesRequest.getServiceId(),
                servicesOfBranchesRequest.getBranchId()
        );
    }

    @Override
    public ServicesOfBranchesResponse deactivateServiceInBranch(ServicesOfBranchesRequest servicesOfBranchesRequest) {
        return servicesOfBranchesService.deactivateServiceInBranch(
                servicesOfBranchesRequest.getServiceId(),
                servicesOfBranchesRequest.getBranchId()
        );
    }

    @Override
    public List<ServiceEntity> getAllByBranchId(Long branchId) {
        branchService.getById(branchId);
        return serviceRepo.findAllByBranchId(branchId);
    }

    @Override
    public List<ServiceResponse> getResponseAllBranchId(Long branchId) {
        branchService.getById(branchId);
        return serviceRepo.findAllByBranchId(branchId)
                .stream()
                .map(service ->  serviceMapper.toResponse(service))
                .toList();
    }

    @Override
    public ServicesOfOffersResponse addServiceToOffer(ServicesOfOffersRequest servicesOfOffersRequest) {

        Offer offer = offerService.getById(servicesOfOffersRequest.getOfferId());

        ServicesOfOffersResponse servicesOfOffersResponse = servicesOfOffersService.addServiceToOffer(
                servicesOfOffersRequest.getServiceId(),
                servicesOfOffersRequest.getOfferId()
        );

        OfferRequest offerRequest = new OfferRequest();
        offerRequest.setPercentageOfDiscount(offer.getPercentageOfDiscount());
        offerRequest.setManagerId(2L);
        offerService.update(offerRequest, offer.getId());

        return servicesOfOffersResponse;
    }

    @Override
    public List<ServiceEntity> getAllByOfferId(Long offerId) {
        Offer offer = offerService.getById(offerId);
        return serviceRepo.findAllByOfferId(offerId);
    }

    @Override
    public List<ServiceResponse> getResponseAllByOfferId(Long offerId) {
        Offer offer = offerService.getById(offerId);
        return serviceRepo.findAllByOfferId(offerId)
                .stream()
                .map(service ->  serviceMapper.toResponse(service))
                .toList();
    }

}
