package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.ServiceRequest;
import com.GP.ELsayes.model.dto.ServiceResponse;
import com.GP.ELsayes.model.entity.relations.ManagersOfServices;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.enums.OperationType;
import com.GP.ELsayes.model.mapper.ServiceMapper;
import com.GP.ELsayes.repository.ServiceRepo;
import com.GP.ELsayes.service.ManagerService;
import com.GP.ELsayes.service.ServiceService;
import com.GP.ELsayes.service.relations.ManagersOfServicesService;
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
    private final ManagersOfServicesService managersOfServicesService;

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
        BeanUtils.copyProperties(existedService,updatedService);
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



}
