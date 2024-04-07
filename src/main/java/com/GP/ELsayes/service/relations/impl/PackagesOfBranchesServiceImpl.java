package com.GP.ELsayes.service.relations.impl;

import com.GP.ELsayes.model.dto.relations.PackageOfBranchesResponse;
import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.Package;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.relations.PackagesOfBranches;
import com.GP.ELsayes.model.enums.Status;
import com.GP.ELsayes.model.mapper.relations.PackagesOfBranchesMapper;
import com.GP.ELsayes.repository.relations.PackagesOfBranchesRepo;
import com.GP.ELsayes.service.BranchService;
import com.GP.ELsayes.service.PackageService;
import com.GP.ELsayes.service.ServiceService;
import com.GP.ELsayes.service.relations.PackagesOfBranchesService;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PackagesOfBranchesServiceImpl implements PackagesOfBranchesService {

    private final PackagesOfBranchesRepo packagesOfBranchesRepo;
    private final PackagesOfBranchesMapper packagesOfBranchesMapper;
    private final PackageService packageService;
    private final BranchService branchService;
    private final ServiceService serviceService;


    public PackagesOfBranchesServiceImpl(PackagesOfBranchesRepo packagesOfBranchesRepo, PackagesOfBranchesMapper packagesOfBranchesMapper,
                                         PackageService packageService, BranchService branchService, @Lazy ServiceService serviceService
    ) {
        this.packagesOfBranchesRepo = packagesOfBranchesRepo;
        this.packagesOfBranchesMapper = packagesOfBranchesMapper;
        this.packageService = packageService;
        this.branchService = branchService;
        this.serviceService = serviceService;
    }



    public PackagesOfBranches getByPackageIdAndBranchId(Long packageId , Long branchId) {
        return packagesOfBranchesRepo.findByPackageIdAndBranchId(packageId,branchId).orElseThrow(
                () -> new NoSuchElementException("There is no package with id = " + packageId + " in this branch")
        );
    }

    private void throwExceptionIfPackageHasAlreadyExistedInBranch(Long packageId , Long branchId){
        Optional<PackagesOfBranches> packagesOfBranch = packagesOfBranchesRepo.findByPackageIdAndBranchId(packageId,branchId);
        if(packagesOfBranch.isEmpty()){
            return;
        }
        throw new RuntimeException("This package with id "+ packagesOfBranch.get().getAPackage().getId() +" already existed in this branch");
    }

    @Override
    public List<ServiceEntity> getAllPackageServicesNotAvailableInBranch(Long packageId, Long branchId) {
        List<ServiceEntity> servicesOfBranch = serviceService.getAllAvailableInBranch(branchId);
        List<ServiceEntity> servicesOfPackage = serviceService.getAllByPackageId(packageId);

        List<ServiceEntity> allPackageServicesNotExistInBranch = new ArrayList<>(servicesOfPackage);
        // Remove all services from allOfferServicesNotExistInBranch included in servicesOfBranch
        allPackageServicesNotExistInBranch.removeAll(servicesOfBranch);

        return allPackageServicesNotExistInBranch;
    }


    private void throwExceptionIfNotAllServicesOfPackageExistInBranch(Long packageId , Long branchId){
        List<ServiceEntity> allPackageServicesNotExistInBranch = getAllPackageServicesNotAvailableInBranch(packageId,branchId);
        if(allPackageServicesNotExistInBranch.isEmpty()){
            return;
        }
        throw new RuntimeException("Failed,This package include service not available in this branch ,Services:"
                  + allPackageServicesNotExistInBranch.stream().map(service -> service.getId()).toList()
        );
    }

    @Override
    @SneakyThrows
    public PackagesOfBranches update(PackagesOfBranches packagesOfBranch){

        PackagesOfBranches updatedOffersOfBranch = packagesOfBranch;
        PackagesOfBranches existedOffersOfBranch = this.getByPackageIdAndBranchId(
                packagesOfBranch.getAPackage().getId(),
                packagesOfBranch.getBranch().getId()
        );


        updatedOffersOfBranch.setId(existedOffersOfBranch.getId());
        BeanUtils.copyProperties(updatedOffersOfBranch,existedOffersOfBranch);

        return packagesOfBranchesRepo.save(updatedOffersOfBranch);
    }

    @Override
    public PackageOfBranchesResponse addPackageToBranch(Long packageId, Long branchId) {
        throwExceptionIfPackageHasAlreadyExistedInBranch(packageId,branchId);
        throwExceptionIfNotAllServicesOfPackageExistInBranch(packageId,branchId);


        Package aPackage = packageService.getById(packageId);
        Branch branch = branchService.getById(branchId);

        PackagesOfBranches packagesOfBranches = new PackagesOfBranches();
        packagesOfBranches.setAPackage(aPackage);
        packagesOfBranches.setBranch(branch);
        packagesOfBranches.setPackageStatus(Status.UNAVAILABLE);
        packagesOfBranches.setAddingDate(new Date());
        packagesOfBranchesRepo.save(packagesOfBranches);

        return packagesOfBranchesMapper.toResponse(packagesOfBranches);
    }

    @Override
    public PackageOfBranchesResponse activatePackageInBranch(Long packageId, Long branchId) {
        throwExceptionIfNotAllServicesOfPackageExistInBranch(packageId,branchId);

        PackagesOfBranches packagesOfBranch = getByPackageIdAndBranchId(
                packageId,
                branchId
        );

        packagesOfBranch.setPackageStatus(Status.AVAILABLE);
        packagesOfBranchesRepo.save(packagesOfBranch);


        return packagesOfBranchesMapper.toResponse(packagesOfBranch);
    }

    @Override
    public PackageOfBranchesResponse deactivatePackageInBranch(Long packageId, Long branchId) {
        PackagesOfBranches packagesOfBranches = getByPackageIdAndBranchId(
                packageId,
                branchId
        );

        packagesOfBranches.setPackageStatus(Status.UNAVAILABLE);
        packagesOfBranchesRepo.save(packagesOfBranches);


        return packagesOfBranchesMapper.toResponse(packagesOfBranches);
    }
}
