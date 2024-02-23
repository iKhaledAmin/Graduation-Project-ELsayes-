package com.GP.ELsayes.repository.relations;

import com.GP.ELsayes.model.entity.relations.OwnersOfManagers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnersOfManagersRepo extends JpaRepository<OwnersOfManagers,Long> {
}
