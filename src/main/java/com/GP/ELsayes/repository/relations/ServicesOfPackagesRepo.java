package com.GP.ELsayes.repository.relations;

import com.GP.ELsayes.model.entity.relations.ServicesOfPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServicesOfPackagesRepo extends JpaRepository<ServicesOfPackage,Long> {

    @Query("SELECT service FROM ServicesOfPackage service WHERE" +
            " service.service.id = :serviceId AND service.packageEntity.id = :packageId")
    Optional<ServicesOfPackage> findByServiceIdAndPackageId(Long serviceId, Long packageId);

}