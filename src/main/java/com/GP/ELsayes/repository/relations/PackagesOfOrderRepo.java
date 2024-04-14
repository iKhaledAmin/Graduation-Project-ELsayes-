package com.GP.ELsayes.repository.relations;

import com.GP.ELsayes.model.entity.relations.PackagesOfOrder;
import com.GP.ELsayes.model.enums.ProgressStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PackagesOfOrderRepo extends JpaRepository<PackagesOfOrder,Long> {
    @Query("SELECT p FROM PackagesOfOrder p WHERE p.packageEntity.id = :packageId AND p.order.id = :orderId")
    Optional<PackagesOfOrder> findByPackageIdAndOrderId(Long packageId, Long orderId);

    @Query("SELECT p FROM PackagesOfOrder p WHERE p.customer.id = :customerId AND p.order.id = :orderId")
    Optional<PackagesOfOrder> findByCustomerIdAndOrderId(Long customerId, Long orderId);

    @Modifying
    @Transactional
    @Query("UPDATE PackagesOfOrder po SET po.progressStatus = 'WAITING' WHERE po.order.id = :orderId")
    void confirmAllPackageOfOrder(Long orderId);


    @Modifying
    @Transactional
    @Query("UPDATE PackagesOfOrder po SET po.progressStatus = :progressStatus WHERE po.order.id = :orderId")
    void updateAllProgressStatusByOrderId(Long orderId, ProgressStatus progressStatus);

    @Query("SELECT p FROM PackagesOfOrder p WHERE p.customer.id = :customerId AND p.progressStatus = 'UN_CONFIRMED'")
    List<PackagesOfOrder> findAllUnConfirmedByCustomerId(Long customerId);

    List<PackagesOfOrder> findAllByOrderId(Long orderId);

//    @Modifying
//    @Query("DELETE FROM PackagesOfOrder po WHERE po.progressStatus = 'UN_CONFIRMED' " +
//            " AND po.customer.id = :customerId")
//    void deleteAllUnConfirmedByCustomerId(Long customerId);
}
