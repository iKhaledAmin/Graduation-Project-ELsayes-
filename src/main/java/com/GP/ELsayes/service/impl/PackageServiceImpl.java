package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.PackageRequest;
import com.GP.ELsayes.model.dto.PackageResponse;
import com.GP.ELsayes.model.dto.relations.PackageOfBranchesRequest;
import com.GP.ELsayes.model.dto.relations.PackageOfBranchesResponse;
import com.GP.ELsayes.model.entity.Package;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.entity.relations.ManagersOfPackages;
import com.GP.ELsayes.model.enums.Status;
import com.GP.ELsayes.model.mapper.PackageMapper;
import com.GP.ELsayes.repository.PackageRepo;
import com.GP.ELsayes.service.BranchService;
import com.GP.ELsayes.service.ManagerService;
import com.GP.ELsayes.service.PackageService;
import com.GP.ELsayes.service.ServiceService;
import com.GP.ELsayes.service.relations.ManagersOfPackagesService;
import com.GP.ELsayes.service.relations.PackagesOfBranchesService;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
 public class PackageServiceImpl implements PackageService {

    private final PackageRepo packageRepo;
    private final PackageMapper packageMapper;
    private final ManagerService managerService;
    private final ServiceService serviceService;
    private final BranchService branchService;
    private final ManagersOfPackagesService managersOfPackagesService;
    private final PackagesOfBranchesService packagesOfBranchesService;


    public PackageServiceImpl(PackageRepo packageRepo, PackageMapper packageMapper, ManagerService managerService, @Lazy ServiceService serviceService, BranchService branchService, ManagersOfPackagesService managersOfPackagesService, @Lazy PackagesOfBranchesService packagesOfBranchesService) {
        this.packageRepo = packageRepo;
        this.packageMapper = packageMapper;
        this.managerService = managerService;
        this.serviceService = serviceService;
        this.branchService = branchService;
        this.managersOfPackagesService = managersOfPackagesService;
        this.packagesOfBranchesService = packagesOfBranchesService;
    }

    void throwExceptionIfNotAllServicesOfPackageAvailableInBranch(Long packageId , Long branchId){

        List<ServiceEntity> servicesOfPackage = serviceService.getAllByPackageId(packageId);
        List<ServiceEntity> servicesAvailableInBranch = serviceService.getAllAvailableInBranch(branchId);

        if(servicesAvailableInBranch.containsAll(servicesOfPackage))
            return;
        throw new RuntimeException("Package with id : " + packageId + " include services not available in this branch");

    }

    @Override
    public String calculateOriginalTotalRequiredTime(Long packageId){
        List<ServiceEntity> serviceList = serviceService.getAllByPackageId(packageId);
        double sum = serviceList.stream()
                .filter(service -> service != null)
                .mapToDouble(service -> Double.parseDouble(service.getRequiredTime()))
                .sum();
        return String.valueOf(sum);
    }

    @Override
    public String calculateOriginalTotalPrice(Long packageId){
        List<ServiceEntity> serviceList = serviceService.getAllByPackageId(packageId);
        double sum = serviceList.stream()
                .filter(service -> service != null)
                .mapToDouble(service -> Double.parseDouble(service.getPrice()))
                .sum();
        return String.valueOf(sum);
    }

    @Override
    public double calculateAmountOfDiscount(Long packageId, String percentageOfDiscount) {
        double originalTotalPrice = Double.parseDouble(calculateOriginalTotalPrice(packageId));
        double discountFactor = Double.parseDouble(percentageOfDiscount) / 100.0;

        double amountOfDiscount = discountFactor *  originalTotalPrice;

        return amountOfDiscount;
    }

    @Override
    public String calculateActualPackagePrice(Long packageId, String percentageOfDiscount) {
        double amountOfDiscount = calculateAmountOfDiscount(packageId,percentageOfDiscount);
        double originalTotalPrice = Double.parseDouble(calculateOriginalTotalPrice(packageId));


        return String.valueOf(originalTotalPrice - amountOfDiscount);
    }

    @Override
    public PackageResponse add(PackageRequest packageRequest) {
        Package aPackage = this.packageMapper.toEntity(packageRequest);

        aPackage = this.packageRepo.save(aPackage);

        Manager manager = managerService.getById(packageRequest.getManagerId());
        ManagersOfPackages managersOfPackages = managersOfPackagesService.addManagerToPackage(
                manager,
                aPackage
        );

        return this.packageMapper.toResponse(aPackage);
    }

    @SneakyThrows
    @Override
    public PackageResponse update(PackageRequest packageRequest, Long packageId) {
        Package existedPackage = this.getById(packageId);
        Package updatedPackage = this.packageMapper.toEntity(packageRequest);

        String originalTotalPrice = calculateOriginalTotalPrice(packageId);
        String originalTotalRequiredTime = calculateOriginalTotalRequiredTime(packageId);
        String currentPackagePrice = calculateActualPackagePrice(packageId, updatedPackage.getPercentageOfDiscount());

        updatedPackage.setId(packageId);
        BeanUtils.copyProperties(updatedPackage, existedPackage);
        updatedPackage.setPercentageOfDiscount(packageRequest.getPercentageOfDiscount());
        updatedPackage.setOriginalTotalPrice(originalTotalPrice);
        updatedPackage.setOriginalTotalRequiredTime(originalTotalRequiredTime);
        updatedPackage.setCurrentPackagePrice(currentPackagePrice);

        updatedPackage = packageRepo.save(updatedPackage);

        Manager manager = managerService.getById(packageRequest.getManagerId());
        ManagersOfPackages managersOfPackages = managersOfPackagesService.updateManagerToPackage(
                manager.getId(),
                updatedPackage.getId()
        );

        return this.packageMapper.toResponse(updatedPackage);
    }


    @Override
    public void delete(Long packageId) {
        this.getById(packageId);
        packageRepo.deleteById(packageId);
    }

    @Override
    public List<PackageResponse> getAll() {
        return packageRepo.findAll()
                .stream()
                .map(aPackage ->  packageMapper.toResponse(aPackage))
                .toList();
    }

    @Override
    public Optional<Package> getObjectById(Long packageId) {
        return packageRepo.findById(packageId);
    }

    @Override
    public Package getById(Long packageId) {
        return getObjectById(packageId).orElseThrow(
                () -> new NoSuchElementException("There is no package with id = " + packageId)
        );
    }

    @Override
    public PackageResponse getResponseById(Long packageId) {
        return packageMapper.toResponse(getById(packageId));
    }

    @Override
    public List<Package> getAllByServiceId(Long serviceId) {
        return packageRepo.findAllByServiceId(serviceId);
    }



    @Override
    public List<PackageResponse> getResponseAllPackagesIncludeService(Long serviceId) {
        return packageRepo.findAllByServiceId(serviceId)
                .stream()
                .map(offer ->  packageMapper.toResponse(offer))
                .toList();
    }

    @Override
    public boolean isAvailableInBranch(Long packageId, Long branchId) {
        Optional<Package> aPackage = packageRepo.findByPackageIdAndBranchIdIfAvailable(packageId, branchId);
        if(aPackage.isEmpty()){
            return false;
        }
        return true;
    }


    @Override
    public PackageOfBranchesResponse addPackageToBranch(PackageOfBranchesRequest packageOfBranchesRequest) {
        throwExceptionIfNotAllServicesOfPackageAvailableInBranch(
            packageOfBranchesRequest.getPackageId(),
            packageOfBranchesRequest.getBranchId()
        );

        return packagesOfBranchesService.addPackageToBranch(
                packageOfBranchesRequest.getPackageId(),
                packageOfBranchesRequest.getBranchId()
        );
    }

    @Override
    public PackageOfBranchesResponse activatePackageInBranch(PackageOfBranchesRequest packageOfBranchesRequest) {
        throwExceptionIfNotAllServicesOfPackageAvailableInBranch(
                packageOfBranchesRequest.getPackageId(),
                packageOfBranchesRequest.getBranchId()
        );

        PackageOfBranchesResponse servicesOfBranchesResponse = packagesOfBranchesService.activatePackageInBranch(
                packageOfBranchesRequest.getPackageId(),
                packageOfBranchesRequest.getBranchId()
        );

        boolean isAvailable = isAvailableInBranch(  packageOfBranchesRequest.getPackageId(), packageOfBranchesRequest.getBranchId());
        servicesOfBranchesResponse.setPackageStatus(isAvailable ? Status.AVAILABLE : Status.UNAVAILABLE);

        return servicesOfBranchesResponse;
    }

    @Override
    public PackageOfBranchesResponse deactivatePackageInBranch(PackageOfBranchesRequest packageOfBranchesRequest) {
        PackageOfBranchesResponse servicesOfBranchesResponse = packagesOfBranchesService.deactivatePackageInBranch(
                packageOfBranchesRequest.getPackageId(),
                packageOfBranchesRequest.getBranchId()
        );

        boolean isAvailable = isAvailableInBranch(  packageOfBranchesRequest.getPackageId(), packageOfBranchesRequest.getBranchId());
        servicesOfBranchesResponse.setPackageStatus(isAvailable ? Status.AVAILABLE : Status.UNAVAILABLE);

        return servicesOfBranchesResponse;
    }

    @Override
    public List<Package> getAllByBranchId(Long branchId) {
        branchService.getById(branchId);
        return packageRepo.findAllByBranchId(branchId);
    }

    @Override
    public List<PackageResponse> getResponseAllByBranchId(Long branchId) {
        branchService.getById(branchId);
        return packageRepo.findAllByBranchId(branchId)
                .stream()
                .map(service -> {
                    PackageResponse response = packageMapper.toResponse(service);
                    boolean isAvailable = isAvailableInBranch(service.getId(), branchId);
                    response.setPackageStatus(isAvailable ? Status.AVAILABLE : Status.UNAVAILABLE);
                    return response;
                })
                .toList();
    }

    @Override
    public Optional<Package> getByServiceIdAndBranchId(Long serviceId, Long branchId) {
        serviceService.getById(serviceId);
        branchService.getById(branchId);
        return packageRepo.findByServiceIdAndBranchId(serviceId,branchId);
    }



}