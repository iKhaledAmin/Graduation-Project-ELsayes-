package com.GP.ELsayes.service.relations;


import com.GP.ELsayes.model.dto.relations.OffersOfBranchesResponse;
import org.springframework.stereotype.Service;

@Service
public interface OffersOfBranchesService {
    OffersOfBranchesResponse addOfferToBranch(Long offerId , Long branchId);
    OffersOfBranchesResponse activateOfferInBranch(Long offerId , Long branchId);
    OffersOfBranchesResponse deactivateOfferInBranch(Long offerId , Long branchId);
}
