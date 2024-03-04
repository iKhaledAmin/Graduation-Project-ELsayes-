package com.GP.ELsayes.repository.relations;

import com.GP.ELsayes.model.entity.relations.WorkersOfBranches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WorkersOfBranchesRepo extends JpaRepository<WorkersOfBranches,Long> {

    @Query("SELECT sob FROM WorkersOfBranches sob WHERE sob.worker.id = :workerId AND sob.branch.id = :branchId")
    public Optional<WorkersOfBranches> findByWorkerIdAndBranchId(Long workerId , Long branchId);

}
