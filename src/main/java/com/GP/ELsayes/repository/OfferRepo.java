package com.GP.ELsayes.repository;

import com.GP.ELsayes.model.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepo extends JpaRepository<Offer,Long> {
}