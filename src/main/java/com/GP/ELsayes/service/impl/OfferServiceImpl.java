package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.BranchResponse;
import com.GP.ELsayes.model.dto.OfferRequest;
import com.GP.ELsayes.model.dto.OfferResponse;
import com.GP.ELsayes.model.entity.Offer;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
import com.GP.ELsayes.model.enums.permissions.OwnerPermission;
import com.GP.ELsayes.model.enums.roles.UserRole;
import com.GP.ELsayes.model.mapper.OfferMapper;
import com.GP.ELsayes.repository.OfferRepo;
import com.GP.ELsayes.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepo offerRepo;
    private final OfferMapper offerMapper;
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
    public OfferResponse add(OfferRequest offerRequest) {
        Offer offer = this.offerMapper.toEntity(offerRequest);
        return this.offerMapper.toResponse(this.offerRepo.save(offer));
    }

    @Override
    public List<OfferResponse> getAll() {
        return offerRepo.findAll()
                .stream()
                .map(offer ->  offerMapper.toResponse(offer))
                .toList();
    }

    @Override
    public OfferResponse update(OfferRequest offerRequest, Long offerId) {
        Offer existedOffer = this.getById(offerId);
        existedOffer = this.offerMapper.toEntity(offerRequest);
        existedOffer.setId(offerId);
        return this.offerMapper.toResponse(offerRepo.save(existedOffer));
    }

    @Override
    public void delete(Long offerId) {
        this.getById(offerId);
        offerRepo.deleteById(offerId);
    }
}