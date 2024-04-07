package com.GP.ELsayes.repository;

import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ManagerRepo extends JpaRepository<Manager,Long> {
    @Query("SELECT manager FROM Manager manager WHERE manager.branch.id = :branchId")
    Optional<Manager> findByBranchId(Long branchId);

    @Query("SELECT manager FROM Manager manager JOIN manager.managersOfPackage mo WHERE mo.aPackage.id = :packageId")
    Optional<Manager> findByPackageId(Long packageId);

}
