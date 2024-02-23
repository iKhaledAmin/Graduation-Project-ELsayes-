package com.GP.ELsayes.repository;

import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



@Repository
public interface CustomerRepo extends JpaRepository<Customer,Long> {

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("UPDATE Car car SET car.customer.id = NULL  WHERE car.customer.id = :customerId")
    void updateCarCustomer( Long customerId);
}
