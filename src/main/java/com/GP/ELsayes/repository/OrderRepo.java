package com.GP.ELsayes.repository;

import com.GP.ELsayes.model.entity.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order,Long> {

    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId AND" +
            " (o.progressStatus = 'WAITING' OR o.progressStatus = 'ON_WORKING')")
    Optional<Order> findUnFinishedByCustomerId(Long customerId);

    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId AND" +
            " (o.progressStatus = 'FINISHED' )")
    Optional<Order> findFinishedByCustomerId(Long customerId);

    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId AND" +
            " (o.progressStatus = 'WAITING' OR o.progressStatus = 'ON_WORKING' OR o.progressStatus = 'FINISHED')")
    Optional<Order> findConfirmedByCustomerId(Long customerId);

    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId AND o.progressStatus = 'UN_CONFIRMED'")
    Optional<Order> findUnConfirmedByCustomerId(Long customerId);

    @Query("SELECT o FROM Order o WHERE o.branch.id = :branchId")
    List<Order> findAllByBranchId(Long branchId);
}

