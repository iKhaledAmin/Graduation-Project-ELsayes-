package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.OfferRequest;
import com.GP.ELsayes.model.dto.OfferResponse;
import com.GP.ELsayes.model.dto.relations.OffersOfBranchesRequest;
import com.GP.ELsayes.model.dto.relations.OffersOfBranchesResponse;
import com.GP.ELsayes.model.entity.Offer;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.entity.relations.ManagersOfOffers;
import com.GP.ELsayes.model.enums.Status;
import com.GP.ELsayes.model.mapper.OfferMapper;
import com.GP.ELsayes.repository.OfferRepo;
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

    private final OfferRepo offerRepo;
    private final OfferMapper offerMapper;
    private final ManagerService managerService;
    private final ServiceService serviceService;
    private final BranchService branchService;
    private final ManagersOfOffersService managersOfOffersService;
    private final OffersOfBranchesService offersOfBranchesService;


    public OfferServiceImpl(OfferRepo offerRepo, OfferMapper offerMapper, ManagerService managerService, @Lazy ServiceService serviceService, BranchService branchService, ManagersOfOffersService managersOfOffersService, @Lazy OffersOfBranchesService offersOfBranchesService) {
        this.offerRepo = offerRepo;
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
    public OfferResponse add(OfferRequest offerRequest) {
        Offer offer = this.offerMapper.toEntity(offerRequest);

        offer = this.offerRepo.save(offer);

        Manager manager = managerService.getById(offerRequest.getManagerId());
        ManagersOfOffers managersOfOffers = managersOfOffersService.addManagerToOffer(
                manager,
                offer
        );

        return this.offerMapper.toResponse(offer);
    }

    @SneakyThrows
    @Override
    public OfferResponse update(OfferRequest offerRequest, Long offerId) {
        Offer existedOffer = this.getById(offerId);
        Offer updatedOffer = this.offerMapper.toEntity(offerRequest);

        String originalTotalPrice = calculateOriginalTotalPrice(offerId);
        String originalTotalRequiredTime = calculateOriginalTotalRequiredTime(offerId);
        String actualOfferPrice = calculateActualOfferPrice(offerId, updatedOffer.getPercentageOfDiscount());

        updatedOffer.setId(offerId);
        BeanUtils.copyProperties(updatedOffer,existedOffer);
        updatedOffer.setPercentageOfDiscount(offerRequest.getPercentageOfDiscount());
        updatedOffer.setOriginalTotalPrice(originalTotalPrice);
        updatedOffer.setOriginalTotalRequiredTime(originalTotalRequiredTime);
        updatedOffer.setActualOfferPrice(actualOfferPrice);

        updatedOffer = offerRepo.save(updatedOffer);

        Manager manager = managerService.getById(offerRequest.getManagerId());
        ManagersOfOffers managersOfOffers = managersOfOffersService.updateManagerToOffer(
                manager.getId(),
                updatedOffer.getId()
        );

        return this.offerMapper.toResponse(updatedOffer);
    }


    @Override
    public void delete(Long offerId) {
        this.getById(offerId);
        offerRepo.deleteById(offerId);
    }

    @Override
    public List<OfferResponse> getAll() {
        return offerRepo.findAll()
                .stream()
                .map(offer ->  offerMapper.toResponse(offer))
                .toList();
    }

    @Override
    public Offer getById(Long offerId) {
        return offerRepo.findById(offerId).orElseThrow(
                () -> new NoSuchElementException("There is no offer with id = " + offerId)
        );
    }

    @Override
    public OfferResponse getResponseById(Long offerId) {
        return offerMapper.toResponse(getById(offerId));
    }

    @Override
    public List<Offer> getAllByServiceId(Long serviceId) {
        return offerRepo.findAllByServiceId(serviceId);
    }



    @Override
    public List<OfferResponse> getResponseAllOffersIncludeService(Long serviceId) {
        return offerRepo.findAllByServiceId(serviceId)
                .stream()
                .map(offer ->  offerMapper.toResponse(offer))
                .toList();
    }

    @Override
    public boolean isAvailableInBranch(Long offerId, Long branchId) {
        Optional<Offer> offer = offerRepo.findByOfferIdAndBranchIdIfAvailable(offerId, branchId);
        if(offer.isEmpty()){
            return false;
        }
        return true;
    }


    @Override
    public OffersOfBranchesResponse addOfferToBranch(OffersOfBranchesRequest offersOfBranchesRequest) {
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
    public OffersOfBranchesResponse activateOfferInBranch(OffersOfBranchesRequest offersOfBranchesRequest) {
        throwExceptionIfNotAllServicesOfOfferAvailableInBranch(
                offersOfBranchesRequest.getOfferId(),
                offersOfBranchesRequest.getBranchId()
        );

        OffersOfBranchesResponse servicesOfBranchesResponse = offersOfBranchesService.activateOfferInBranch(
                offersOfBranchesRequest.getOfferId(),
                offersOfBranchesRequest.getBranchId()
        );

        boolean isAvailable = isAvailableInBranch(  offersOfBranchesRequest.getOfferId(), offersOfBranchesRequest.getBranchId());
        servicesOfBranchesResponse.setOfferStatus(isAvailable ? Status.AVAILABLE : Status.UNAVAILABLE);

        return servicesOfBranchesResponse;
    }

    @Override
    public OffersOfBranchesResponse deactivateOfferInBranch(OffersOfBranchesRequest offersOfBranchesRequest) {
        OffersOfBranchesResponse servicesOfBranchesResponse = offersOfBranchesService.deactivateOfferInBranch(
                offersOfBranchesRequest.getOfferId(),
                offersOfBranchesRequest.getBranchId()
        );

        boolean isAvailable = isAvailableInBranch(  offersOfBranchesRequest.getOfferId(), offersOfBranchesRequest.getBranchId());
        servicesOfBranchesResponse.setOfferStatus(isAvailable ? Status.AVAILABLE : Status.UNAVAILABLE);

        return servicesOfBranchesResponse;
    }

    @Override
    public List<Offer> getAllByBranchId(Long branchId) {
        branchService.getById(branchId);
        return offerRepo.findAllByBranchId(branchId);
    }

    @Override
    public List<OfferResponse> getResponseAllBranchId(Long branchId) {
        branchService.getById(branchId);
        return offerRepo.findAllByBranchId(branchId)
                .stream()
                .map(service -> {
                    OfferResponse response = offerMapper.toResponse(service);
                    boolean isAvailable = isAvailableInBranch(service.getId(), branchId);
                    response.setOfferStatus(isAvailable ? Status.AVAILABLE : Status.UNAVAILABLE);
                    return response;
                })
                .toList();
    }

    @Override
    public Optional<Offer> getByServiceIdAndBranchId(Long serviceId, Long branchId) {
        serviceService.getById(serviceId);
        branchService.getById(branchId);
        return offerRepo.findByServiceIdAndBranchId(serviceId,branchId);
    }



}