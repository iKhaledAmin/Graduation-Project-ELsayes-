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
}