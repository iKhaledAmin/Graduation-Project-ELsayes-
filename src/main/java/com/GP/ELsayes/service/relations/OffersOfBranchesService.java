package com.GP.ELsayes.service.relations;


import com.GP.ELsayes.model.dto.relations.OffersOfBranchesResponse;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.relations.OffersOfBranches;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OffersOfBranchesService {
    OffersOfBranches getByOfferIdAndBranchId(Long offerId, Long branchId);
    public List<ServiceEntity> getAllOfferServicesNotAvailableInBranch(Long offerId, Long branchId);
    OffersOfBranchesResponse addOfferToBranch(Long offerId , Long branchId);
    OffersOfBranchesResponse activateOfferInBranch(Long offerId , Long branchId);
    OffersOfBranchesResponse deactivateOfferInBranch(Long offerId , Long branchId);

    OffersOfBranches update(OffersOfBranches offersOfBranch);
}

