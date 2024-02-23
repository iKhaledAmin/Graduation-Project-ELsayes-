package com.GP.ELsayes.repository.relations;

import com.GP.ELsayes.model.entity.relations.ManagersOfServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagersOfServicesRepo extends JpaRepository<ManagersOfServices,Long> {
}
