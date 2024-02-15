package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.ServiceRequest;
import com.GP.ELsayes.model.dto.ServiceResponse;
import com.GP.ELsayes.model.mapper.ServiceMapper;
import com.GP.ELsayes.repository.ServiceRepo;
import com.GP.ELsayes.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.GP.ELsayes.model.entity.ServiceEntity;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class ServiceServiceImpl implements ServiceService {

    private final ServiceMapper serviceMapper;
    private final ServiceRepo serviceRepo;
    @Override
    public ServiceEntity getById(Long serviceId) {
        return serviceRepo.findById(serviceId).orElseThrow(
                () -> new NoSuchElementException("There are no service with id = " + serviceId)
        );
    }

    @Override
    public ServiceResponse add(ServiceRequest serviceRequest) {
        ServiceEntity service = this.serviceMapper.toEntity(serviceRequest);
        return this.serviceMapper.toResponse(this.serviceRepo.save(service));
    }

    @Override
    public List<ServiceResponse> getAll() {
        return serviceRepo.findAll()
                .stream()
                .map(owner ->  serviceMapper.toResponse(owner))
                .toList();
    }

    @Override
    public ServiceResponse update(ServiceRequest serviceRequest, Long serviceId) {
        ServiceEntity existedService = this.getById(serviceId);
        existedService = this.serviceMapper.toEntity(serviceRequest);
        existedService.setId(serviceId);
        return this.serviceMapper.toResponse(serviceRepo.save(existedService));
    }

    @Override
    public void delete(Long serviceId) {
        getById(serviceId);
        serviceRepo.deleteById(serviceId);
    }
}
