package com.GP.ELsayes.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnersOfBranchesRepo extends JpaRepository<com.GP.ELsayes.model.entity.OwnersOfBranches,Long> {
}
