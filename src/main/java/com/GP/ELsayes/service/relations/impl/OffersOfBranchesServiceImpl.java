package com.GP.ELsayes.service.relations.impl;

import com.GP.ELsayes.model.dto.relations.OffersOfBranchesResponse;
import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.Offer;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.relations.OffersOfBranches;
import com.GP.ELsayes.model.enums.Status;
import com.GP.ELsayes.model.mapper.relations.OffersOfBranchesMapper;
import com.GP.ELsayes.repository.relations.OffersOfBranchesRepo;
import com.GP.ELsayes.service.BranchService;
import com.GP.ELsayes.service.OfferService;
import com.GP.ELsayes.service.ServiceService;
import com.GP.ELsayes.service.relations.OffersOfBranchesService;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OffersOfBranchesServiceImpl implements OffersOfBranchesService {

    private final OffersOfBranchesRepo offersOfBranchesRepo;
    private final OffersOfBranchesMapper offersOfBranchesMapper;
    private final OfferService offerService;
    private final BranchService branchService;
    private final ServiceService serviceService;


    public OffersOfBranchesServiceImpl(OffersOfBranchesRepo offersOfBranchesRepo, OffersOfBranchesMapper offersOfBranchesMapper,
                                        OfferService offerService, BranchService branchService, @Lazy ServiceService serviceService
    ) {
        this.offersOfBranchesRepo = offersOfBranchesRepo;
        this.offersOfBranchesMapper = offersOfBranchesMapper;
        this.offerService = offerService;
        this.branchService = branchService;
        this.serviceService = serviceService;
    }



    public OffersOfBranches getByOfferIdAndBranchId(Long offerId , Long branchId) {
        return offersOfBranchesRepo.findByOfferIdAndBranchId(offerId,branchId).orElseThrow(
                () -> new NoSuchElementException("There is no offer with id = " + offerId + " in this branch")
        );
    }

    private void throwExceptionIfOfferHasAlreadyExistedInBranch(Long offerId , Long branchId){
        Optional<OffersOfBranches> offersOfBranch = offersOfBranchesRepo.findByOfferIdAndBranchId(offerId,branchId);
        if(offersOfBranch.isEmpty()){
            return;
        }
        throw new RuntimeException("This offer with id "+ offersOfBranch.get().getOffer().getId() +" already existed in this branch");
    }

    @Override
    public List<ServiceEntity> getAllOfferServicesNotAvailableInBranch(Long offerId, Long branchId) {
        List<ServiceEntity> servicesOfBranch = serviceService.getAllAvailableInBranch(branchId);
        List<ServiceEntity> servicesOfOffer = serviceService.getAllByOfferId(offerId);

        List<ServiceEntity> allOfferServicesNotExistInBranch = new ArrayList<>(servicesOfOffer);
        // Remove all services from allOfferServicesNotExistInBranch included in servicesOfBranch
        allOfferServicesNotExistInBranch.removeAll(servicesOfBranch);

        return allOfferServicesNotExistInBranch;
    }


    private void throwExceptionIfNotAllServicesOfOfferExistInBranch(Long offerId , Long branchId){
        List<ServiceEntity> allOfferServicesNotExistInBranch = getAllOfferServicesNotAvailableInBranch(offerId,branchId);
        if(allOfferServicesNotExistInBranch.isEmpty()){
            return;
        }
        throw new RuntimeException("Failed,This offer include service not available in this branch ,Services:"
                  + allOfferServicesNotExistInBranch.stream().map(service -> service.getId()).toList()
        );
    }

    @Override
    @SneakyThrows
    public OffersOfBranches update(OffersOfBranches offersOfBranch){

        OffersOfBranches updatedOffersOfBranch = offersOfBranch;
        OffersOfBranches existedOffersOfBranch = this.getByOfferIdAndBranchId(
                offersOfBranch.getOffer().getId(),
                offersOfBranch.getBranch().getId()
        );


        updatedOffersOfBranch.setId(existedOffersOfBranch.getId());
        BeanUtils.copyProperties(updatedOffersOfBranch,existedOffersOfBranch);

        return offersOfBranchesRepo.save(updatedOffersOfBranch);
    }

    @Override
    public OffersOfBranchesResponse addOfferToBranch(Long offerId, Long branchId) {
        throwExceptionIfOfferHasAlreadyExistedInBranch(offerId,branchId);
        throwExceptionIfNotAllServicesOfOfferExistInBranch(offerId,branchId);


        Offer offer = offerService.getById(offerId);
        Branch branch = branchService.getById(branchId);

        OffersOfBranches offersOfBranches = new OffersOfBranches();
        offersOfBranches.setOffer(offer);
        offersOfBranches.setBranch(branch);
        offersOfBranches.setOfferStatus(Status.UNAVAILABLE);
        offersOfBranches.setAddingDate(new Date());
        offersOfBranchesRepo.save(offersOfBranches);

        return offersOfBranchesMapper.toResponse(offersOfBranches);
    }

    @Override
    public OffersOfBranchesResponse activateOfferInBranch(Long offerId, Long branchId) {
        throwExceptionIfNotAllServicesOfOfferExistInBranch(offerId,branchId);

        OffersOfBranches offersOfBranch = getByOfferIdAndBranchId(
                offerId,
                branchId
        );

        offersOfBranch.setOfferStatus(Status.AVAILABLE);
        offersOfBranchesRepo.save(offersOfBranch);


        return offersOfBranchesMapper.toResponse(offersOfBranch);
    }

    @Override
    public OffersOfBranchesResponse deactivateOfferInBranch(Long offerId, Long branchId) {
        OffersOfBranches offersOfBranches = getByOfferIdAndBranchId(
                offerId,
                branchId
        );

        offersOfBranches.setOfferStatus(Status.UNAVAILABLE);
        offersOfBranchesRepo.save(offersOfBranches);


        return offersOfBranchesMapper.toResponse(offersOfBranches);
    }
}
