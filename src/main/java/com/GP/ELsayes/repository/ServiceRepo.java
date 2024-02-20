package com.GP.ELsayes.repository;

import com.GP.ELsayes.model.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepo extends JpaRepository<ServiceEntity,Long> {
}
