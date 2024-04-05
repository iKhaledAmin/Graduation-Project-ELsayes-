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
}
