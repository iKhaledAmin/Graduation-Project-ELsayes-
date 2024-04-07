package com.GP.ELsayes.repository.relations;

import com.GP.ELsayes.model.entity.relations.PackagesOfBranches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PackagesOfBranchesRepo extends JpaRepository<PackagesOfBranches,Long> {

   // @Query("SELECT OoB FROM PackagesOfBranches OoB WHERE OoB.aPackage.id = :packageId AND OoB.branch.id = :branchId")
    @Query("SELECT packageBranch FROM PackagesOfBranches packageBranch WHERE" +
            " packageBranch.aPackage.id = :packageId AND packageBranch.branch.id = :branchId")
    Optional<PackagesOfBranches> findByPackageIdAndBranchId(Long packageId, Long branchId);

}
