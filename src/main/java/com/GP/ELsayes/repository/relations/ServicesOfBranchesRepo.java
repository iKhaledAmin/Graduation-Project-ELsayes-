package com.GP.ELsayes.repository.relations;

import com.GP.ELsayes.model.entity.relations.ServicesOfBranches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ServicesOfBranchesRepo extends JpaRepository<ServicesOfBranches,Long> {

    @Query("SELECT sob FROM ServicesOfBranches sob WHERE sob.service.id = :serviceId AND sob.branch.id = :branchId")
    public Optional<ServicesOfBranches> findByServiceIdAndBranchId(Long serviceId , Long branchId);
}
