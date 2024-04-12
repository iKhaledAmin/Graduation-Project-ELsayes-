package com.GP.ELsayes.service.relations.impl;

import com.GP.ELsayes.model.dto.relations.ServicesOfPackagesResponse;
import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.Package;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.relations.PackagesOfBranches;
import com.GP.ELsayes.model.entity.relations.ServicesOfPackage;
import com.GP.ELsayes.model.enums.Status;
import com.GP.ELsayes.model.mapper.relations.ServicesOfPackagesMapper;
import com.GP.ELsayes.repository.relations.ServicesOfPackagesRepo;
import com.GP.ELsayes.service.BranchService;
import com.GP.ELsayes.service.PackageService;
import com.GP.ELsayes.service.ServiceService;
import com.GP.ELsayes.service.relations.PackagesOfBranchesService;
import com.GP.ELsayes.service.relations.ServicesOfPackagesService;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ServicesOfPackagesServiceImpl implements ServicesOfPackagesService {

    private final ServicesOfPackagesRepo servicesOfPackagesRepo;
    private final ServicesOfPackagesMapper servicesOfPackagesMapper;
    private final ServiceService serviceService;
    private final PackageService packageService;

    private final BranchService branchService;
    private final PackagesOfBranchesService servicesOfBranchesService;



    public ServicesOfPackagesServiceImpl(ServicesOfPackagesRepo servicesOfPackagesRepo, ServicesOfPackagesMapper servicesOfPackagesMapper, @Lazy ServiceService serviceService, @Lazy PackageService packageService, BranchService branchService, PackagesOfBranchesService servicesOfBranchesService) {
        this.servicesOfPackagesRepo = servicesOfPackagesRepo;
        this.servicesOfPackagesMapper = servicesOfPackagesMapper;
        this.serviceService = serviceService;
        this.packageService = packageService;
        this.branchService = branchService;

        this.servicesOfBranchesService = servicesOfBranchesService;
    }

    private ServicesOfPackage getByServiceIdAndBranchId(Long serviceId , Long packageId) {
        return servicesOfPackagesRepo.findByServiceIdAndPackageId(serviceId,packageId).orElseThrow(
                () -> new NoSuchElementException("There is no service with id = " + serviceId + " in this package")
        );
    }

    @Override
    public void handleAvailabilityOfPackageInAllBranches(Long packageId, Long serviceId){
        List<Branch> branches = branchService.getAllByOfferId(packageId);

        branches.forEach(branch -> {
            if(serviceService.isAvailableInBranch(serviceId , branch.getId()) == false){
                PackagesOfBranches packagesOfBranch = new PackagesOfBranches();
                packagesOfBranch = servicesOfBranchesService.getByPackageIdAndBranchId(packageId,branch.getId());
                packagesOfBranch.setPackageStatus(Status.UNAVAILABLE);
                servicesOfBranchesService.update(packagesOfBranch);
            }
        });
    }


    private void throwExceptionIfServiceHasAlreadyExistedInPackage(Long serviceId , Long packageId){
        Optional<ServicesOfPackage> servicesOfPackage = servicesOfPackagesRepo.findByServiceIdAndPackageId(serviceId,packageId);
        if(servicesOfPackage.isEmpty()){
            return;
        }
        throw new RuntimeException("This service with id "+ servicesOfPackage.get().getService().getId() +" already existed in this package");
    }

    @SneakyThrows
    private ServicesOfPackage update(ServicesOfPackage servicesOfPackage){

        ServicesOfPackage updatedServicesOfPackage = servicesOfPackage;
        ServicesOfPackage existedServicesOfPackage = this.getByServiceIdAndBranchId(
                servicesOfPackage.getService().getId(),
                servicesOfPackage.getPackageEntity().getId()
        );


        updatedServicesOfPackage.setId(existedServicesOfPackage.getId());
        BeanUtils.copyProperties(updatedServicesOfPackage,existedServicesOfPackage);

        return servicesOfPackagesRepo.save(updatedServicesOfPackage);
    }

    @Override
    public ServicesOfPackagesResponse addServiceToPackage(Long serviceId, Long packageId) {
        throwExceptionIfServiceHasAlreadyExistedInPackage(serviceId, packageId);

        ServiceEntity service = serviceService.getById(serviceId);
        Package aPackage = packageService.getById(packageId);

        ServicesOfPackage servicesOfPackage = new ServicesOfPackage();
        servicesOfPackage.setService(service);
        servicesOfPackage.setPackageEntity(aPackage);
        servicesOfPackage.setAddingDate(new Date());
        servicesOfPackagesRepo.save(servicesOfPackage);

        handleAvailabilityOfPackageInAllBranches(packageId,serviceId);

        return servicesOfPackagesMapper.toResponse(servicesOfPackage);
    }



}
