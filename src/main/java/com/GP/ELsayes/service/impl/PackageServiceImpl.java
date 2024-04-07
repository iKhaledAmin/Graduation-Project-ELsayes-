package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.PackageRequest;
import com.GP.ELsayes.model.dto.PackageResponse;
import com.GP.ELsayes.model.dto.relations.OffersOfBranchesRequest;
import com.GP.ELsayes.model.dto.relations.PackageOfBranchesResponse;
import com.GP.ELsayes.model.entity.Package;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.entity.relations.ManagersOfPackages;
import com.GP.ELsayes.model.enums.Status;
import com.GP.ELsayes.model.mapper.OfferMapper;
import com.GP.ELsayes.repository.PackageRepo;
import com.GP.ELsayes.service.BranchService;
import com.GP.ELsayes.service.ManagerService;
import com.GP.ELsayes.service.OfferService;
import com.GP.ELsayes.service.ServiceService;
import com.GP.ELsayes.service.relations.ManagersOfOffersService;
import com.GP.ELsayes.service.relations.OffersOfBranchesService;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
 public class OfferServiceImpl implements OfferService {

    private final PackageRepo packageRepo;
    private final OfferMapper offerMapper;
    private final ManagerService managerService;
    private final ServiceService serviceService;
    private final BranchService branchService;
    private final ManagersOfOffersService managersOfOffersService;
    private final OffersOfBranchesService offersOfBranchesService;


    public OfferServiceImpl(PackageRepo packageRepo, OfferMapper offerMapper, ManagerService managerService, @Lazy ServiceService serviceService, BranchService branchService, ManagersOfOffersService managersOfOffersService, @Lazy OffersOfBranchesService offersOfBranchesService) {
        this.packageRepo = packageRepo;
        this.offerMapper = offerMapper;
        this.managerService = managerService;
        this.serviceService = serviceService;
        this.branchService = branchService;
        this.managersOfOffersService = managersOfOffersService;
        this.offersOfBranchesService = offersOfBranchesService;
    }

    void throwExceptionIfNotAllServicesOfOfferAvailableInBranch(Long offerId , Long branchId){

        List<ServiceEntity> servicesOfOffer = serviceService.getAllByOfferId(offerId);
        List<ServiceEntity> servicesAvailableInBranch = serviceService.getAllAvailableInBranch(branchId);

        if(servicesAvailableInBranch.containsAll(servicesOfOffer))
            return;
        throw new RuntimeException("Offer with id : " + offerId + " include services not available in this branch");

    }

    @Override
    public String calculateOriginalTotalRequiredTime(Long offerId){
        List<ServiceEntity> serviceList = serviceService.getAllByOfferId(offerId);
        double sum = serviceList.stream()
                .filter(service -> service != null)
                .mapToDouble(service -> Double.parseDouble(service.getRequiredTime()))
                .sum();
        return String.valueOf(sum);
    }

    @Override
    public String calculateOriginalTotalPrice(Long offerId){
        List<ServiceEntity> serviceList = serviceService.getAllByOfferId(offerId);
        double sum = serviceList.stream()
                .filter(service -> service != null)
                .mapToDouble(service -> Double.parseDouble(service.getPrice()))
                .sum();
        return String.valueOf(sum);
    }

    @Override
    public double calculateAmountOfDiscount(Long offerId , String percentageOfDiscount) {
        double originalTotalPrice = Double.parseDouble(calculateOriginalTotalPrice(offerId));
        double discountFactor = Double.parseDouble(percentageOfDiscount) / 100.0;

        double amountOfDiscount = discountFactor *  originalTotalPrice;

        return amountOfDiscount;
    }

    @Override
    public String calculateActualOfferPrice(Long offerId, String percentageOfDiscount) {
        double amountOfDiscount = calculateAmountOfDiscount(offerId,percentageOfDiscount);
        double originalTotalPrice = Double.parseDouble(calculateOriginalTotalPrice(offerId));


        return String.valueOf(originalTotalPrice - amountOfDiscount);
    }

    @Override
    public PackageResponse add(PackageRequest packageRequest) {
        Package aPackage = this.offerMapper.toEntity(packageRequest);

        aPackage = this.packageRepo.save(aPackage);

        Manager manager = managerService.getById(packageRequest.getManagerId());
        ManagersOfPackages managersOfPackages = managersOfOffersService.addManagerToOffer(
                manager,
                aPackage
        );

        return this.offerMapper.toResponse(aPackage);
    }

    @SneakyThrows
    @Override
    public PackageResponse update(PackageRequest packageRequest, Long offerId) {
        Package existedPackage = this.getById(offerId);
        Package updatedPackage = this.offerMapper.toEntity(packageRequest);

        String originalTotalPrice = calculateOriginalTotalPrice(offerId);
        String originalTotalRequiredTime = calculateOriginalTotalRequiredTime(offerId);
        String currentPackagePrice = calculateActualOfferPrice(offerId, updatedPackage.getPercentageOfDiscount());

        updatedPackage.setId(offerId);
        BeanUtils.copyProperties(updatedPackage, existedPackage);
        updatedPackage.setPercentageOfDiscount(packageRequest.getPercentageOfDiscount());
        updatedPackage.setOriginalTotalPrice(originalTotalPrice);
        updatedPackage.setOriginalTotalRequiredTime(originalTotalRequiredTime);
        updatedPackage.setCurrentPackagePrice(currentPackagePrice);

        updatedPackage = packageRepo.save(updatedPackage);

        Manager manager = managerService.getById(packageRequest.getManagerId());
        ManagersOfPackages managersOfPackages = managersOfOffersService.updateManagerToOffer(
                manager.getId(),
                updatedPackage.getId()
        );

        return this.offerMapper.toResponse(updatedPackage);
    }


    @Override
    public void delete(Long offerId) {
        this.getById(offerId);
        packageRepo.deleteById(offerId);
    }

    @Override
    public List<PackageResponse> getAll() {
        return packageRepo.findAll()
                .stream()
                .map(offer ->  offerMapper.toResponse(offer))
                .toList();
    }

    @Override
    public Optional<Package> getObjectById(Long offerId) {
        return packageRepo.findById(offerId);
    }

    @Override
    public Package getById(Long offerId) {
        return getObjectById(offerId).orElseThrow(
                () -> new NoSuchElementException("There is no offer with id = " + offerId)
        );
    }

    @Override
    public PackageResponse getResponseById(Long offerId) {
        return offerMapper.toResponse(getById(offerId));
    }

    @Override
    public List<Package> getAllByServiceId(Long serviceId) {
        return packageRepo.findAllByServiceId(serviceId);
    }



    @Override
    public List<PackageResponse> getResponseAllOffersIncludeService(Long serviceId) {
        return packageRepo.findAllByServiceId(serviceId)
                .stream()
                .map(offer ->  offerMapper.toResponse(offer))
                .toList();
    }

    @Override
    public boolean isAvailableInBranch(Long offerId, Long branchId) {
        Optional<Package> offer = packageRepo.findByPackageIdAndBranchIdIfAvailable(offerId, branchId);
        if(offer.isEmpty()){
            return false;
        }
        return true;
    }


    @Override
    public PackageOfBranchesResponse addOfferToBranch(OffersOfBranchesRequest offersOfBranchesRequest) {
        throwExceptionIfNotAllServicesOfOfferAvailableInBranch(
            offersOfBranchesRequest.getOfferId(),
            offersOfBranchesRequest.getBranchId()
        );

        return offersOfBranchesService.addOfferToBranch(
                offersOfBranchesRequest.getOfferId(),
                offersOfBranchesRequest.getBranchId()
        );
    }

    @Override
    public PackageOfBranchesResponse activateOfferInBranch(OffersOfBranchesRequest offersOfBranchesRequest) {
        throwExceptionIfNotAllServicesOfOfferAvailableInBranch(
                offersOfBranchesRequest.getOfferId(),
                offersOfBranchesRequest.getBranchId()
        );

        PackageOfBranchesResponse servicesOfBranchesResponse = offersOfBranchesService.activateOfferInBranch(
                offersOfBranchesRequest.getOfferId(),
                offersOfBranchesRequest.getBranchId()
        );

        boolean isAvailable = isAvailableInBranch(  offersOfBranchesRequest.getOfferId(), offersOfBranchesRequest.getBranchId());
        servicesOfBranchesResponse.setPackageStatus(isAvailable ? Status.AVAILABLE : Status.UNAVAILABLE);

        return servicesOfBranchesResponse;
    }

    @Override
    public PackageOfBranchesResponse deactivateOfferInBranch(OffersOfBranchesRequest offersOfBranchesRequest) {
        PackageOfBranchesResponse servicesOfBranchesResponse = offersOfBranchesService.deactivateOfferInBranch(
                offersOfBranchesRequest.getOfferId(),
                offersOfBranchesRequest.getBranchId()
        );

        boolean isAvailable = isAvailableInBranch(  offersOfBranchesRequest.getOfferId(), offersOfBranchesRequest.getBranchId());
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
                    PackageResponse response = offerMapper.toResponse(service);
                    boolean isAvailable = isAvailableInBranch(service.getId(), branchId);
                    response.setOfferStatus(isAvailable ? Status.AVAILABLE : Status.UNAVAILABLE);
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