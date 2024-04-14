package com.GP.ELsayes.repository.relations;

import com.GP.ELsayes.model.entity.relations.ServicesOfOrders;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicesOfOrderRepo extends JpaRepository<ServicesOfOrders,Long> {
    Optional<ServicesOfOrders> findByServiceIdAndOrderId(Long serviceId, Long orderId);

    @Modifying
    @Transactional
    @Query("UPDATE ServicesOfOrders so SET so.progressStatus = 'WAITING' WHERE so.order.id = :orderId")
    void confirmAllServiceOfOrder(Long orderId);

    List<ServicesOfOrders> findObjectByOrderId(Long orderId);

    @Query("SELECT so FROM ServicesOfOrders so WHERE so.service.id = :serviceId AND so.customer.id = :customerId")
    List<ServicesOfOrders> findByServiceIdAndCustomerId(Long serviceId, Long customerId);

    @Query("SELECT so FROM ServicesOfOrders so WHERE so.progressStatus = 'ON_WORKING' AND so.customer.id = :customerId AND so.worker.id = :workerId ")
    Optional<ServicesOfOrders> findByCustomerIdAndWorkerId(Long customerId, Long workerId);


    @Query("SELECT so FROM ServicesOfOrders so WHERE so.packagesOfOrder.id = :packageOfOrderId AND so.progressStatus = 'UN_CONFIRMED'")
    List<ServicesOfOrders> findAllUnConfirmedByPackageOrderId(Long packageOfOrderId);

    @Query("SELECT so FROM ServicesOfOrders so WHERE so.packagesOfOrder.id = :packageOfOrderId " +
            "AND so.order.id = :orderId AND so.packagesOfOrder IS NOT NULL")
    List<ServicesOfOrders> findObjectByOrderIdAndPackageOfOrderId(Long orderId, Long packageOfOrderId);


    @Query("SELECT so FROM ServicesOfOrders so WHERE so.progressStatus = 'UN_CONFIRMED' " +
            "AND so.packagesOfOrder.id is null AND so.customer.id = :customerId")
    List<ServicesOfOrders> findAllUnConfirmedByCustomerId(Long customerId);


    @Query("SELECT so FROM ServicesOfOrders so WHERE so.order.customer.id = :customerId " +
            "AND so.order.progressStatus IN ('WAITING', 'ON_WORKING', 'FINISHED')")
    List<ServicesOfOrders> findAllConfirmedByCustomerId(Long customerId);

    List<ServicesOfOrders> findAllByOrderId(Long orderId);

//    @Modifying
//    @Query("SELECT so FROM ServicesOfOrders so WHERE so.progressStatus = 'UN_CONFIRMED' " +
//            "AND so.packagesOfOrder.id IS NULL AND so.customer.id = :customerId")
//    void deleteAllUnConfirmedByCustomerId(Long customerId);
}
