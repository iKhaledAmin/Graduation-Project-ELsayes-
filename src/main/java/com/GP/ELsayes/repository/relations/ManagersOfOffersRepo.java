package com.GP.ELsayes.repository.relations;

import com.GP.ELsayes.model.entity.relations.ManagersOfOffers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagersOfOffersRepo extends JpaRepository<ManagersOfOffers,Long> {
    @Query("SELECT mo FROM ManagersOfOffers mo WHERE mo.manager.id = :managerId AND mo.offer.id = :offerId")
    Optional<ManagersOfOffers>findByManagerIdAndOfferId(Long managerId, Long offerId);
}

