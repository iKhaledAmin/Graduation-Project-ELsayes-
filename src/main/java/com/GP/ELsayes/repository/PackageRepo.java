package com.GP.ELsayes.repository;

import com.GP.ELsayes.model.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PackageRepo extends JpaRepository<Package,Long> {


    @Query("SELECT pkg FROM Package pkg JOIN pkg.servicesOfPackage servicePkg WHERE servicePkg.service.id = :serviceId")
    List<Package> findAllByServiceId(Long serviceId);

    @Query("SELECT pkg FROM Package pkg JOIN pkg.packagesOfBranch branchPkg WHERE branchPkg.branch.id = :branchId")
    List<Package> findAllByBranchId(Long branchId);

    @Query("SELECT pkg FROM Package pkg JOIN pkg.servicesOfPackage servicePkg JOIN pkg.packagesOfBranch branchPkg WHERE servicePkg.service.id = :serviceId AND branchPkg.branch.id = :branchId")
    Optional<Package> findByServiceIdAndBranchId(Long serviceId, Long branchId);

    @Query("SELECT pkg FROM Package pkg JOIN pkg.packagesOfBranch branchPkg WHERE branchPkg.packageStatus = 'AVAILABLE' AND branchPkg.packageEntity.id = :packageId AND branchPkg.branch.id = :branchId")
    Optional<Package> findByPackageIdAndBranchIdIfAvailable(Long packageId, Long branchId);

    @Query("SELECT pkg FROM Package pkg JOIN pkg.packagesOfBranch branchPkg WHERE branchPkg.packageEntity.id = :packageId AND branchPkg.branch.id = :branchId")

    Package findByPackageIdAndBranchId(Long packageId, Long branchId);
}
