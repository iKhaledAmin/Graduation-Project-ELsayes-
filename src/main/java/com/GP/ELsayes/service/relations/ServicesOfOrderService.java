package com.GP.ELsayes.service.relations;

public interface ServicesOfOrderService {
    void addServiceToOrder(Long customerId,Long ServiceId);
    void confirmAllServiceOfOrder(Long orderId);
}
