package com.GP.ELsayes.repository.relations;

import com.GP.ELsayes.model.entity.relations.OwnersOfRestrictedOwners;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnersOfRestrictedOwnersRepo extends JpaRepository<OwnersOfRestrictedOwners,Long> {
    @Query("select o from OwnersOfRestrictedOwners o where o.oldOwner.id IS NULL")
    Optional<OwnersOfRestrictedOwners> findTneMainOwner();
}
