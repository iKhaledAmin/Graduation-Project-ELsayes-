package com.GP.ELsayes.repository;

import com.GP.ELsayes.model.entity.OwnersOfManagers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnersOfManagersRepo extends JpaRepository<OwnersOfManagers,Long> {
}
