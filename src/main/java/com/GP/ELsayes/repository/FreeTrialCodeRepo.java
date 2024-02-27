package com.GP.ELsayes.repository;

import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.FreeTrialCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FreeTrialCodeRepo extends JpaRepository<FreeTrialCode,Long> {
    Optional<FreeTrialCode> findByWorkerId(Long workerId);

}
