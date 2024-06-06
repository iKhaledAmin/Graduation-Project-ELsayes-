package com.GP.ELsayes.service.relations;

import com.GP.ELsayes.model.dto.ServicesOfOrderResponse;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.GP.ELsayes.model.entity.relations.ServicesOfOrders;

import java.util.List;
import java.util.Optional;

public interface ServicesOfOrderService {
    public void addServiceToOrder(Long customerId, Long serviceId ,Boolean inPackage);
    public void addServiceToOrder(Long customerId, Long serviceId );
    public void confirmAllServiceOfOrder(Long orderId);

    public List<ServicesOfOrders> getAlltByOrderId(Long orderId);
    public Optional<ServicesOfOrders> getUnConfirmedByOrderIdAndServiceId(Long orderId, Long serviceId);
    public void setWorkerServiceTask(Long customerId, Long serviceId, Worker worker);

    public void finishServiceTask(Long customerId, Long workerId);

    public List<ServicesOfOrders> getAllUnConfirmedByPackageOfOrderId(Long packageOfOrderId);
    //  public List<ServicesOfOrderResponse> getResponseAllUnConfirmedServicesByCustomerId(Long customerId);
    public List<ServicesOfOrderResponse> getResponseAllConfirmedByCustomerId(Long customerId);
    public List<ServicesOfOrderResponse> getUnConfirmedServicesOfOrderByCustomerId(Long customerId, Long branchId);

    List<ServicesOfOrderResponse> getResponseAllByOrderId(Long orderId);

    public void deleteServiceFromOrderList(Long serviceOfOrderId);

}
