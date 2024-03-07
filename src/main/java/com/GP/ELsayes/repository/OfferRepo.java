package com.GP.ELsayes.repository;

import com.GP.ELsayes.model.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;

public interface OfferRepo extends JpaRepository<Offer,Long> {


    @Query("SELECT o FROM Offer o JOIN o.servicesOfOffer so WHERE so.service.id = :serviceId")
    List<Offer> findAllByServiceId(Long serviceId);
}