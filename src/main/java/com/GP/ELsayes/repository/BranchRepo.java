package com.GP.ELsayes.repository;

import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BranchRepo extends JpaRepository<Branch,Long> {
    @Query("SELECT branch FROM Branch branch WHERE branch.manager.id = :managerId")
    Optional<Branch> findByManagerId(Long managerId);

    @Query("SELECT b FROM Branch b JOIN b.WorkersOfBranch w WHERE w.worker.id = :workerId")
    Optional<Branch> findByWorkerId(Long workerId);

    @Query("select b from Branch b join b.offerOfBranch bo join bo.offer o where o.id = :offerId")
    List<Branch> findAllByOfferId(Long offerId);
    @Query("select b from Branch b join b.servicesOfBranch bs join bs.service s where s.id = :serviceId")
    List<Branch> findAllByServiceId(Long serviceId);
}
