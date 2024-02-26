package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.BranchResponse;
import com.GP.ELsayes.model.dto.OfferRequest;
import com.GP.ELsayes.model.dto.OfferResponse;
import com.GP.ELsayes.model.entity.Offer;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
import com.GP.ELsayes.model.entity.relations.ManagersOfOffers;
import com.GP.ELsayes.model.entity.relations.ManagersOfServices;
import com.GP.ELsayes.model.entity.relations.OwnersOfManagers;
import com.GP.ELsayes.model.enums.OperationType;
import com.GP.ELsayes.model.enums.permissions.OwnerPermission;
import com.GP.ELsayes.model.enums.roles.UserRole;
import com.GP.ELsayes.model.mapper.OfferMapper;
import com.GP.ELsayes.repository.OfferRepo;
import com.GP.ELsayes.service.ManagerService;
import com.GP.ELsayes.service.OfferService;
import com.GP.ELsayes.service.relations.ManagersOfOffersService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepo offerRepo;
    private final OfferMapper offerMapper;
    private final ManagerService managerService;
    private final ManagersOfOffersService managersOfOffersService;


    @Override
    public OfferResponse add(OfferRequest offerRequest) {
        Offer offer = this.offerMapper.toEntity(offerRequest);
        offer = this.offerRepo.save(offer);

        Manager manager = managerService.getById(offerRequest.getManagerId());
        ManagersOfOffers managersOfOffers = managersOfOffersService.save(
                manager,
                offer,
                OperationType.CREATE
        );

        return this.offerMapper.toResponse(offer);
    }

    @SneakyThrows
    @Override
    public OfferResponse update(OfferRequest offerRequest, Long offerId) {
        Offer existedOffer = this.getById(offerId);
        Offer updatedOffer = this.offerMapper.toEntity(offerRequest);

        updatedOffer.setId(offerId);
        BeanUtils.copyProperties(existedOffer,updatedOffer);
        updatedOffer = offerRepo.save(existedOffer);

        Manager manager = managerService.getById(offerRequest.getManagerId());
        ManagersOfOffers managersOfOffers = managersOfOffersService.save(
                manager,
                updatedOffer,
                OperationType.UPDATE
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



}