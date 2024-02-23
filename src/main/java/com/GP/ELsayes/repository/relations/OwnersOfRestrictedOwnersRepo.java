package com.GP.ELsayes.repository.relations;

import com.GP.ELsayes.model.entity.relations.OwnersOfRestrictedOwners;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnersOfRestrictedOwnersRepo extends JpaRepository<OwnersOfRestrictedOwners,Long> {

}
