package com.GP.ELsayes.repository;

import com.GP.ELsayes.model.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OfferRepo extends JpaRepository<Package,Long> {


    @Query("SELECT o FROM Offer o JOIN o.servicesOfOffer so WHERE so.service.id = :serviceId")
    List<Package> findAllByServiceId(Long serviceId);

    @Query("SELECT o FROM Offer o JOIN o.offersOfBranch sb WHERE sb.branch.id = :branchId")
    List<Package> findAllByBranchId(Long branchId);

    @Query("SELECT o FROM Offer o JOIN o.servicesOfOffer so JOIN o.offersOfBranch ob WHERE so.service.id = :serviceId AND ob.branch.id = :branchId")
   Optional<Package>  findByServiceIdAndBranchId(Long serviceId, Long branchId);

    @Query("select o from Offer o join o.offersOfBranch ob where ob.offerStatus = 'AVAILABLE' and ob.offer.id = :offerId and ob.branch.id = :branchId")
    Optional<Package> findByOfferIdAndBranchIdIfAvailable(Long offerId, Long branchId);
}
