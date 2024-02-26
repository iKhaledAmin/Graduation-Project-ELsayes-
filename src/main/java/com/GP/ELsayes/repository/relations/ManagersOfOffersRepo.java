package com.GP.ELsayes.repository.relations;

import com.GP.ELsayes.model.entity.relations.ManagersOfOffers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagersOfOffersRepo extends JpaRepository<ManagersOfOffers,Long> {
}
