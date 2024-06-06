package com.GP.ELsayes.service;

import com.GP.ELsayes.model.dto.PackageRequest;
import com.GP.ELsayes.model.dto.PackageResponse;
import com.GP.ELsayes.model.dto.relations.PackageOfBranchesRequest;
import com.GP.ELsayes.model.dto.relations.PackageOfBranchesResponse;
import com.GP.ELsayes.model.entity.Package;

import java.util.List;
import java.util.Optional;

public interface PackageService extends CrudService<PackageRequest, Package, PackageResponse,Long> {
    public PackageResponse add(PackageRequest packageRequest, List<Long> serviceIds);

    public List<PackageResponse> getResponseAllPackagesIncludeService(Long serviceId);

    boolean isAvailableInBranch(Long packageId, Long branchId) ;
    public List<Package> getAllByServiceId(Long serviceId);
    public String calculateOriginalTotalRequiredTime(Long packageId);
    public String calculateOriginalTotalPrice(Long packageId);
    public double calculateAmountOfDiscount(Long packageId, String percentageOfDiscount);
    public String calculateActualPackagePrice(Long packageId , String percentageOfDiscount);

    public PackageOfBranchesResponse addPackageToBranch(PackageOfBranchesRequest packageOfBranchesRequest);
    public PackageOfBranchesResponse activatePackageInBranch(PackageOfBranchesRequest packageOfBranchesRequest);
    public PackageOfBranchesResponse deactivatePackageInBranch(PackageOfBranchesRequest packageOfBranchesRequest);

    public List<Package> getAllByBranchId(Long branchId);

    public PackageResponse toResponseAccordingToBranch(Long packageId, Long branchId);
    public PackageResponse getByPackageIdOrByPackageIdAndBranchId(Long packageId, Long branchId);
    public List<PackageResponse> getResponseAllByBranchId(Long branchId);

    Optional<Package> getByServiceIdAndBranchId(Long serviceId, Long branchId);
    public void incrementProfit(Long packageId);
}