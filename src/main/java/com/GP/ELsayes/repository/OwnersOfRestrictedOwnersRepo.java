package com.GP.ELsayes.repository;

import com.GP.ELsayes.model.entity.OwnersOfRestrictedOwners;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnersOfRestrictedOwnersRepo extends JpaRepository<OwnersOfRestrictedOwners,Long> {

    OwnersOfRestrictedOwners getByOldOwnerId(Long oldOwnerId);
}
