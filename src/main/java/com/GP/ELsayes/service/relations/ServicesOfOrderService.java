package com.GP.ELsayes.service.relations;

import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.GP.ELsayes.model.entity.relations.ServicesOfOrders;

import java.util.List;

public interface ServicesOfOrderService {
    void addServiceToOrder(Long customerId,Long ServiceId);
    void confirmAllServiceOfOrder(Long orderId);

    List<ServicesOfOrders> getObjectByOrderId(Long orderId);
    public void setWorkerServiceTask(Long customerId, Long serviceId, Worker worker);
}
