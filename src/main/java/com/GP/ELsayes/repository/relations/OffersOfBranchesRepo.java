package com.GP.ELsayes.repository.relations;

import com.GP.ELsayes.model.entity.relations.OffersOfBranches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OffersOfBranchesRepo extends JpaRepository<OffersOfBranches,Long> {

    @Query("SELECT OoB FROM OffersOfBranches OoB WHERE OoB.offer.id = :offerId AND OoB.branch.id = :branchId")
    Optional<OffersOfBranches> findByOfferIdAndBranchId(Long offerId, Long branchId);

}
