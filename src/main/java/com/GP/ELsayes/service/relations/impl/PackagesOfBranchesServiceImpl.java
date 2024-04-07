package com.GP.ELsayes.service.relations.impl;

import com.GP.ELsayes.model.dto.relations.PackageOfBranchesResponse;
import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.Package;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.relations.PackagesOfBranches;
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



    public PackagesOfBranches getByOfferIdAndBranchId(Long offerId , Long branchId) {
        return offersOfBranchesRepo.findByPackageIdAndBranchId(offerId,branchId).orElseThrow(
                () -> new NoSuchElementException("There is no offer with id = " + offerId + " in this branch")
        );
    }

    private void throwExceptionIfOfferHasAlreadyExistedInBranch(Long offerId , Long branchId){
        Optional<PackagesOfBranches> offersOfBranch = offersOfBranchesRepo.findByPackageIdAndBranchId(offerId,branchId);
        if(offersOfBranch.isEmpty()){
            return;
        }
        throw new RuntimeException("This offer with id "+ offersOfBranch.get().getAPackage().getId() +" already existed in this branch");
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
    public PackagesOfBranches update(PackagesOfBranches offersOfBranch){

        PackagesOfBranches updatedOffersOfBranch = offersOfBranch;
        PackagesOfBranches existedOffersOfBranch = this.getByOfferIdAndBranchId(
                offersOfBranch.getAPackage().getId(),
                offersOfBranch.getBranch().getId()
        );


        updatedOffersOfBranch.setId(existedOffersOfBranch.getId());
        BeanUtils.copyProperties(updatedOffersOfBranch,existedOffersOfBranch);

        return offersOfBranchesRepo.save(updatedOffersOfBranch);
    }

    @Override
    public PackageOfBranchesResponse addOfferToBranch(Long offerId, Long branchId) {
        throwExceptionIfOfferHasAlreadyExistedInBranch(offerId,branchId);
        throwExceptionIfNotAllServicesOfOfferExistInBranch(offerId,branchId);


        Package aPackage = offerService.getById(offerId);
        Branch branch = branchService.getById(branchId);

        PackagesOfBranches packagesOfBranches = new PackagesOfBranches();
        packagesOfBranches.setAPackage(aPackage);
        packagesOfBranches.setBranch(branch);
        packagesOfBranches.setPackageStatus(Status.UNAVAILABLE);
        packagesOfBranches.setAddingDate(new Date());
        offersOfBranchesRepo.save(packagesOfBranches);

        return offersOfBranchesMapper.toResponse(packagesOfBranches);
    }

    @Override
    public PackageOfBranchesResponse activateOfferInBranch(Long offerId, Long branchId) {
        throwExceptionIfNotAllServicesOfOfferExistInBranch(offerId,branchId);

        PackagesOfBranches offersOfBranch = getByOfferIdAndBranchId(
                offerId,
                branchId
        );

        offersOfBranch.setPackageStatus(Status.AVAILABLE);
        offersOfBranchesRepo.save(offersOfBranch);


        return offersOfBranchesMapper.toResponse(offersOfBranch);
    }

    @Override
    public PackageOfBranchesResponse deactivateOfferInBranch(Long offerId, Long branchId) {
        PackagesOfBranches packagesOfBranches = getByOfferIdAndBranchId(
                offerId,
                branchId
        );

        packagesOfBranches.setPackageStatus(Status.UNAVAILABLE);
        offersOfBranchesRepo.save(packagesOfBranches);


        return offersOfBranchesMapper.toResponse(packagesOfBranches);
    }
}
