package com.GP.ELsayes.repository.relations;

import com.GP.ELsayes.model.entity.relations.ServicesOfOffers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServicesOfOffersRepo extends JpaRepository<ServicesOfOffers,Long> {

    @Query("SELECT service FROM ServicesOfOffers service WHERE service.service.id = :serviceId AND service.offer.id = :offerId")
    Optional<ServicesOfOffers> findByServiceIdAndOfferId(Long serviceId, Long offerId);

}