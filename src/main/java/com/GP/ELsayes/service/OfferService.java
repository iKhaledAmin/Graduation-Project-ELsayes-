package com.GP.ELsayes.service;

import com.GP.ELsayes.model.dto.OfferRequest;
import com.GP.ELsayes.model.dto.OfferResponse;
import com.GP.ELsayes.model.dto.relations.OffersOfBranchesRequest;
import com.GP.ELsayes.model.dto.relations.OffersOfBranchesResponse;
import com.GP.ELsayes.model.entity.Offer;

import java.util.List;
import java.util.Optional;

public interface OfferService extends CrudService<OfferRequest, Offer, OfferResponse,Long> {

    public List<OfferResponse> getResponseAllOffersIncludeService(Long serviceId);
    public List<Offer> getAllOffersIncludeService(Long serviceId);
    public String calculateOriginalTotalRequiredTime(Long offerId);
    public String calculateOriginalTotalPrice(Long offerId);
    public double calculateAmountOfDiscount(Long offerId, String percentageOfDiscount);
    public String calculateActualOfferPrice(Long offerId , String percentageOfDiscount);

    public OffersOfBranchesResponse addOfferToBranch(OffersOfBranchesRequest offersOfBranchesRequest);
    public OffersOfBranchesResponse activateOfferInBranch(OffersOfBranchesRequest offersOfBranchesRequest);
    public OffersOfBranchesResponse deactivateOfferInBranch(OffersOfBranchesRequest offersOfBranchesRequest);

    public List<Offer> getAllByBranchId(Long branchId);
    public List<OfferResponse> getResponseAllBranchId(Long branchId);

    Optional<Offer> getByServiceIdAndBranchId(Long serviceId, Long branchId);
}