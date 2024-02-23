package com.GP.ELsayes.repository.relations;


import com.GP.ELsayes.model.entity.relations.OwnersOfBranches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnersOfBranchesRepo extends JpaRepository<OwnersOfBranches,Long> {
}
