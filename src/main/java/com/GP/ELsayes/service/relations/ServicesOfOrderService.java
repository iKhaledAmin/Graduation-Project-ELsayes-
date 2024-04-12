package com.GP.ELsayes.service.relations;

import com.GP.ELsayes.model.dto.ServicesOfOrderResponse;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.GP.ELsayes.model.entity.relations.ServicesOfOrders;

import java.util.List;

public interface ServicesOfOrderService {
    public void addServiceToOrder(Long customerId, Long serviceId ,Boolean inPackage);
    public void addServiceToOrder(Long customerId, Long serviceId );
    public void deleteServiceFromOrderList(Long serviceOfOrderId);
    public void confirmAllServiceOfOrder(Long orderId);

    public List<ServicesOfOrders> getObjectByOrderId(Long orderId);
    public void setWorkerServiceTask(Long customerId, Long serviceId, Worker worker);

    public void finishServiceTask(Long customerId, Long workerId);

    public List<ServicesOfOrders> getAllUnConfirmedByPackageOfOrderId(Long packageOfOrderId);
    public List<ServicesOfOrderResponse> getAllUnConfirmedByCustomerId(Long customerId);
    public List<ServicesOfOrderResponse> getAllConfirmedByCustomerId(Long customerId);
}
