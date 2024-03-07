package com.GP.ELsayes.repository;

import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.sound.sampled.Line;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepo extends JpaRepository<ServiceEntity,Long> {

    @Query("select s from ServiceEntity s join s.servicesOfBranch sb join sb.branch b where b.id = :branchId")
    List<ServiceEntity> findAllByBranchId(Long branchId);

    @Query("select s from ServiceEntity s join s.servicesOfOffer so join so.offer o where o.id = :offerId")
    List<ServiceEntity> findAllByOfferId(Long offerId);


}
