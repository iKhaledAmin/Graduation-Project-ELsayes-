package com.GP.ELsayes.repository;

import com.GP.ELsayes.model.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface OfferRepo extends JpaRepository<Offer,Long> {


    @Query("SELECT o FROM Offer o JOIN o.servicesOfOffer so WHERE so.service.id = :serviceId")
    List<Offer> findAllByServiceId(Long serviceId);

    @Query("SELECT o FROM Offer o JOIN o.offersOfBranch sb WHERE sb.branch.id = :branchId")
    List<Offer> findAllByBranchId(Long branchId);

    @Query("SELECT o FROM Offer o JOIN o.servicesOfOffer so JOIN o.offersOfBranch ob WHERE so.service.id = :serviceId AND ob.branch.id = :branchId")
   Optional<Offer>  findByServiceIdAndBranchId(Long serviceId, Long branchId);
}
