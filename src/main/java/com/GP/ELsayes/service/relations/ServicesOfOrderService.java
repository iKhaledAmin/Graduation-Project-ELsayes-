package com.GP.ELsayes.service.relations;

import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.GP.ELsayes.model.entity.relations.ServicesOfOrders;

import java.util.List;

public interface ServicesOfOrderService {
    public void addServiceToOrder(Long customerId, Long serviceId ,Boolean inPackage);
    public void addServiceToOrder(Long customerId, Long serviceId );
    public void deleteServiceFromOrderList(Long serviceOfOrderId);
    void confirmAllServiceOfOrder(Long orderId);

    List<ServicesOfOrders> getObjectByOrderId(Long orderId);
    public void setWorkerServiceTask(Long customerId, Long serviceId, Worker worker);

    void finishServiceTask(Long customerId, Long workerId);

    List<ServicesOfOrders> getAllUnConfirmedByPackageOfOrderId(Long packageOfOrderId);
}
