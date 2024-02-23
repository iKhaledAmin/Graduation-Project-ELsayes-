package com.GP.ELsayes.repository;

import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface OwnerRepo extends JpaRepository<Owner, Long> {

   // @Query("UPDATE Car car SET car.customer.id = NULL  WHERE car.customer.id = :customerId")


    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("UPDATE OwnersOfRestrictedOwners owner SET owner.oldOwner.id = NULL  WHERE owner.oldOwner.id = :ownerId")
    void deleteAllRelationsWithOtherOwners( Long ownerId);
}