package com.GP.ELsayes.service.relations.impl;

import com.GP.ELsayes.model.dto.relations.ServicesOfBranchesResponse;
import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.Package;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.relations.ServicesOfBranches;
import com.GP.ELsayes.model.enums.Status;
import com.GP.ELsayes.model.mapper.relations.ServicesOfBranchesMapper;
import com.GP.ELsayes.repository.relations.ServicesOfBranchesRepo;
import com.GP.ELsayes.service.BranchService;
import com.GP.ELsayes.service.PackageService;
import com.GP.ELsayes.service.ServiceService;
import com.GP.ELsayes.service.relations.ServicesOfBranchesService;
import jakarta.persistence.EntityNotFoundException;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
//@RequiredArgsConstructor
public class ServicesOfBranchesServiceImpl implements ServicesOfBranchesService {
    private static ServicesOfBranchesRepo servicesOfBranchesRepo = null;
    private final ServiceService serviceService;
    private final BranchService branchService;
    private final PackageService packageService;
    private final ServicesOfBranchesMapper servicesOfBranchesMapper;
    private final ServicesOfPackagesServiceImpl servicesOfOffersService;


    public ServicesOfBranchesServiceImpl(ServicesOfBranchesRepo servicesOfBranchesRepo, @Lazy ServiceService serviceService, BranchService branchService, @Lazy PackageService packageService, ServicesOfBranchesMapper servicesOfBranchesMapper, @Lazy ServicesOfPackagesServiceImpl servicesOfOffersService) {
        this.servicesOfBranchesRepo = servicesOfBranchesRepo;
        this.serviceService = serviceService;
        this.branchService = branchService;
        this.packageService = packageService;
        this.servicesOfBranchesMapper = servicesOfBranchesMapper;
        this.servicesOfOffersService = servicesOfOffersService;
    }



    public static ServicesOfBranches getByServiceIdAndBranchId(Long serviceId , Long branchId) {
        return servicesOfBranchesRepo.findByServiceIdAndBranchId(serviceId,branchId).orElseThrow(
                () -> new NoSuchElementException("There is no service with id = " + serviceId + " in this branch")
        );
    }

    private void throwExceptionIfServiceHasAlreadyExistedInBranch(Long serviceId , Long branchId){
        Optional<ServicesOfBranches> servicesOfBranches = servicesOfBranchesRepo.findByServiceIdAndBranchId(serviceId,branchId);
        if(servicesOfBranches.isEmpty()){
            return;
        }
        throw new RuntimeException("This service with id "+ servicesOfBranches.get().getService().getId() +" already existed in this branch");
    }

    @SneakyThrows
    private ServicesOfBranches update(ServicesOfBranches servicesOfBranches){

        ServicesOfBranches updatedServiceInBranch = servicesOfBranches;
        ServicesOfBranches existedServiceInBranch = this.getByServiceIdAndBranchId(
                servicesOfBranches.getService().getId(),
                servicesOfBranches.getBranch().getId()
        );


        updatedServiceInBranch.setId(existedServiceInBranch.getId());
        BeanUtils.copyProperties(updatedServiceInBranch,existedServiceInBranch);

        return servicesOfBranchesRepo.save(updatedServiceInBranch);
    }

    @Override
    public ServicesOfBranchesResponse addServiceToBranch(Long serviceId, Long branchId) {
        throwExceptionIfServiceHasAlreadyExistedInBranch(serviceId,branchId);

        ServicesOfBranches servicesOfBranches = new ServicesOfBranches();

        ServiceEntity service = serviceService.getById(serviceId);
        Branch branch = branchService.getById(branchId);

        servicesOfBranches.setService(service);
        servicesOfBranches.setBranch(branch);
        servicesOfBranches.setServiceStatus(Status.UNAVAILABLE);
        servicesOfBranches.setAddingDate(new Date());

        servicesOfBranchesRepo.save(servicesOfBranches);

        return servicesOfBranchesMapper.toResponse(servicesOfBranches);
    }



    @Override
    public ServicesOfBranchesResponse activateServiceInBranch(Long serviceId, Long branchId) {
        ServicesOfBranches servicesOfBranches = getByServiceIdAndBranchId(
                serviceId,
                branchId
        );

        servicesOfBranches.setServiceStatus(Status.AVAILABLE);
        servicesOfBranchesRepo.save(servicesOfBranches);


        return servicesOfBranchesMapper.toResponse(servicesOfBranches);
    }



    @Override
    public ServicesOfBranchesResponse deactivateServiceInBranch(Long serviceId, Long branchId) {
        Optional<ServicesOfBranches> servicesOfBranchesOptional = Optional.ofNullable(getByServiceIdAndBranchId(serviceId, branchId));

        if (!servicesOfBranchesOptional.isPresent()) {
            // Handle the case where the service is not found in the branch
            throw new EntityNotFoundException("Service with id "+ serviceId + " not found in this branch");
        }

        ServicesOfBranches servicesOfBranches = servicesOfBranchesOptional.get();
        servicesOfBranches.setServiceStatus(Status.UNAVAILABLE);
        servicesOfBranchesRepo.save(servicesOfBranches);

        Optional<Package> offerOptional = packageService.getByServiceIdAndBranchId(serviceId, branchId);
        if (offerOptional.isPresent()) {
            servicesOfOffersService.handleAvailabilityOfPackageInAllBranches(offerOptional.get().getId(), serviceId);
        }

        return servicesOfBranchesMapper.toResponse(servicesOfBranches);
    }
}
