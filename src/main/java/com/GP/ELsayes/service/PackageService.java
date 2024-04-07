package com.GP.ELsayes.service;

import com.GP.ELsayes.model.dto.PackageRequest;
import com.GP.ELsayes.model.dto.PackageResponse;
import com.GP.ELsayes.model.dto.relations.OffersOfBranchesRequest;
import com.GP.ELsayes.model.dto.relations.PackageOfBranchesResponse;
import com.GP.ELsayes.model.entity.Package;

import java.util.List;
import java.util.Optional;

public interface OfferService extends CrudService<PackageRequest, Package, PackageResponse,Long> {

    public List<PackageResponse> getResponseAllOffersIncludeService(Long serviceId);

    boolean isAvailableInBranch(Long offerId, Long branchId) ;
    public List<Package> getAllByServiceId(Long serviceId);
    public String calculateOriginalTotalRequiredTime(Long offerId);
    public String calculateOriginalTotalPrice(Long offerId);
    public double calculateAmountOfDiscount(Long offerId, String percentageOfDiscount);
    public String calculateActualOfferPrice(Long offerId , String percentageOfDiscount);

    public PackageOfBranchesResponse addOfferToBranch(OffersOfBranchesRequest offersOfBranchesRequest);
    public PackageOfBranchesResponse activateOfferInBranch(OffersOfBranchesRequest offersOfBranchesRequest);
    public PackageOfBranchesResponse deactivateOfferInBranch(OffersOfBranchesRequest offersOfBranchesRequest);

    public List<Package> getAllByBranchId(Long branchId);
    public List<PackageResponse> getResponseAllByBranchId(Long branchId);

    Optional<Package> getByServiceIdAndBranchId(Long serviceId, Long branchId);
}