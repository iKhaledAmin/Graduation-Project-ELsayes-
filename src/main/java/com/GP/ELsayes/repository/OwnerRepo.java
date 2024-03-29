package com.GP.ELsayes.repository;

import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface OwnerRepo extends JpaRepository<Owner, Long> {


    @Query("select o from Owner o join o.ownersOfManagers om where om.manager.id = :managerId")
    Optional<Owner> findByManagerId(Long managerId);



//    //@Query("select o from Owner o join o.ownersOfRestrictedOwners1 om where om.oldOwner.id IS NULL")
//    @Query("select o from Owner o where o.id not in" +
//            " (select om.oldOwner.id from OwnersOfRestrictedOwners om where om.oldOwner is not null)")
//    Optional<Owner> findTneMainOwner();
}