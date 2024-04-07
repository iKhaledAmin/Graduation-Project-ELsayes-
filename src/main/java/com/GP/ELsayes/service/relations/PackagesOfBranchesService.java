package com.GP.ELsayes.service.relations;


import com.GP.ELsayes.model.dto.relations.PackageOfBranchesResponse;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.relations.PackagesOfBranches;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OffersOfBranchesService {
    PackagesOfBranches getByOfferIdAndBranchId(Long offerId, Long branchId);
    public List<ServiceEntity> getAllOfferServicesNotAvailableInBranch(Long offerId, Long branchId);
    PackageOfBranchesResponse addOfferToBranch(Long offerId , Long branchId);
    PackageOfBranchesResponse activateOfferInBranch(Long offerId , Long branchId);
    PackageOfBranchesResponse deactivateOfferInBranch(Long offerId , Long branchId);

    PackagesOfBranches update(PackagesOfBranches offersOfBranch);
}

