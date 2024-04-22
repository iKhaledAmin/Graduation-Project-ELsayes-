package com.GP.ELsayes.repository;

import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.GP.ELsayes.model.enums.roles.WorkerRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WorkerRepo extends JpaRepository<Worker, Long> {


    @Query("SELECT w FROM Worker w WHERE w.branch.id = :branchId")
        Optional<List<Worker>> findAllWorkersByBranchId(Long branchId);

    @Query("SELECT COUNT(w) FROM Worker w WHERE w.branch.id = :branchId")
    Integer getNumberOfWorkersByBranchId(Long branchId);

    @Query("SELECT w FROM Worker w WHERE w.workerStatus = 'AVAILABLE' AND w.workerRole = :workerRole ORDER BY w.score DESC")
    List<Worker> findAllAvailableWorkerByWorkerRoleOrderByScore(WorkerRole workerRole);

    Optional<Worker> findByEmail(String username);

    Optional<Worker> findByUserName(String userName);
}
