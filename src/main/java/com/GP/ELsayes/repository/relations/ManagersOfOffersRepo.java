package com.GP.ELsayes.repository.relations;

import com.GP.ELsayes.model.entity.relations.ManagersOfPackages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagersOfOffersRepo extends JpaRepository<ManagersOfPackages,Long> {
    @Query("SELECT mop FROM ManagersOfPackages mop WHERE mop.manager.id = :managerId AND mop.aPackage.id = :packageId")
    Optional<ManagersOfPackages> findByManagerIdAndPackageId(Long managerId, Long packageId);
}

