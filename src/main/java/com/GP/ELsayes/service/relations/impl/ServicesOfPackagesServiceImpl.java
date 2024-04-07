package com.GP.ELsayes.service.relations.impl;

import com.GP.ELsayes.model.dto.relations.ServicesOfPackagesResponse;
import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.Package;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.relations.PackagesOfBranches;
import com.GP.ELsayes.model.entity.relations.ServicesOfPackage;
import com.GP.ELsayes.model.enums.Status;
import com.GP.ELsayes.model.mapper.relations.ServicesOfOffersMapper;
import com.GP.ELsayes.repository.relations.ServicesOfPackagesRepo;
import com.GP.ELsayes.service.BranchService;
import com.GP.ELsayes.service.PackageService;
import com.GP.ELsayes.service.ServiceService;
import com.GP.ELsayes.service.relations.PackagesOfBranchesService;
import com.GP.ELsayes.service.relations.ServicesOfOffersService;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ServicesOfOffersServiceImpl implements ServicesOfOffersService {

    private final ServicesOfPackagesRepo servicesOfPackagesRepo;
    private final ServicesOfOffersMapper servicesOfOffersMapper;
    private final ServiceService serviceService;
    private final PackageService packageService;

    private final BranchService branchService;
    private final PackagesOfBranchesService servicesOfBranchesService;



    public ServicesOfOffersServiceImpl(ServicesOfPackagesRepo servicesOfPackagesRepo, ServicesOfOffersMapper servicesOfOffersMapper, @Lazy ServiceService serviceService, @Lazy PackageService packageService, BranchService branchService, PackagesOfBranchesService servicesOfBranchesService) {
        this.servicesOfPackagesRepo = servicesOfPackagesRepo;
        this.servicesOfOffersMapper = servicesOfOffersMapper;
        this.serviceService = serviceService;
        this.packageService = packageService;
        this.branchService = branchService;

        this.servicesOfBranchesService = servicesOfBranchesService;
    }

    private ServicesOfPackage getByServiceIdAndBranchId(Long serviceId , Long offerId) {
        return servicesOfPackagesRepo.findByServiceIdAndPackageId(serviceId,offerId).orElseThrow(
                () -> new NoSuchElementException("There is no service with id = " + serviceId + " in this offer")
        );
    }

    @Override
    public void handleAvailabilityOfOfferInAllBranches(Long offerId , Long serviceId){
        List<Branch> branches = branchService.getAllByOfferId(offerId);

        branches.forEach(branch -> {
            if(serviceService.isAvailableInBranch(serviceId , branch.getId()) == false){
                PackagesOfBranches offersOfBranch = new PackagesOfBranches();
                offersOfBranch = servicesOfBranchesService.getByPackageIdAndBranchId(offerId ,branch.getId());
                offersOfBranch.setPackageStatus(Status.UNAVAILABLE);
                servicesOfBranchesService.update(offersOfBranch);
            }
        });
    }


    private void throwExceptionIfServiceHasAlreadyExistedInOffer(Long serviceId , Long offerId){
        Optional<ServicesOfPackage> servicesOfOffer = servicesOfPackagesRepo.findByServiceIdAndPackageId(serviceId,offerId);
        if(servicesOfOffer.isEmpty()){
            return;
        }
        throw new RuntimeException("This service with id "+ servicesOfOffer.get().getService().getId() +" already existed in this offer");
    }

    @SneakyThrows
    private ServicesOfPackage update(ServicesOfPackage servicesOfOffer){

        ServicesOfPackage updatedServicesOfOffer = servicesOfOffer;
        ServicesOfPackage existedServicesOfOffer = this.getByServiceIdAndBranchId(
                servicesOfOffer.getService().getId(),
                servicesOfOffer.getAPackage().getId()
        );


        updatedServicesOfOffer.setId(existedServicesOfOffer.getId());
        BeanUtils.copyProperties(updatedServicesOfOffer,existedServicesOfOffer);

        return servicesOfPackagesRepo.save(updatedServicesOfOffer);
    }

    @Override
    public ServicesOfPackagesResponse addServiceToOffer(Long serviceId, Long offerId) {
        throwExceptionIfServiceHasAlreadyExistedInOffer(serviceId,offerId);

        ServiceEntity service = serviceService.getById(serviceId);
        Package aPackage = packageService.getById(offerId);

        ServicesOfPackage servicesOfOffer = new ServicesOfPackage();
        servicesOfOffer.setService(service);
        servicesOfOffer.setAPackage(aPackage);
        servicesOfOffer.setAddingDate(new Date());
        servicesOfPackagesRepo.save(servicesOfOffer);

        handleAvailabilityOfOfferInAllBranches(offerId,serviceId);

        return servicesOfOffersMapper.toResponse(servicesOfOffer);
    }



}
