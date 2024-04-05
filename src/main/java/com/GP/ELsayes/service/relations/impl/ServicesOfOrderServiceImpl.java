package com.GP.ELsayes.service.relations.impl;

import com.GP.ELsayes.model.entity.Order;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.GP.ELsayes.model.entity.relations.ServicesOfOrders;
import com.GP.ELsayes.model.enums.ProgressStatus;
import com.GP.ELsayes.model.enums.WorkerStatus;
import com.GP.ELsayes.repository.relations.ServicesOfOrderRepo;
import com.GP.ELsayes.service.CustomerService;
import com.GP.ELsayes.service.OrderService;
import com.GP.ELsayes.service.ServiceService;
import com.GP.ELsayes.service.WorkerService;
import com.GP.ELsayes.service.relations.ServicesOfOrderService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ServicesOfOrderServiceImpl implements ServicesOfOrderService {

    private final ServicesOfOrderRepo servicesOfOrderRepo;
    private final OrderService orderService;
    private final CustomerService customerService;
    private final ServiceService serviceService;
    private final WorkerService workerService;

    private void throwExceptionIfServiceHasAlreadyExistedInOrder(Long serviceId , Long orderId){
        Optional<ServicesOfOrders> servicesOfOrder = servicesOfOrderRepo.findByServiceIdAndOrderId(serviceId,orderId);
        if(servicesOfOrder.isEmpty()){
            return;
        }
        throw new RuntimeException("This service with id "+ serviceId +" already existed in this order");
    }


    public Optional<ServicesOfOrders> getObjectFirstByServiceIdAndCustomerId(Long serviceId, Long customerId) {
        List<ServicesOfOrders> servicesOfOrders = servicesOfOrderRepo.findByServiceIdAndCustomerId(serviceId, customerId);
        if (!servicesOfOrders.isEmpty()) {
            return Optional.of(servicesOfOrders.get(0));
        }
        return Optional.empty();
    }


    @Override
    public void addServiceToOrder(Long customerId, Long serviceId) {
        Customer customer = customerService.getObjectById(customerId).get();
        ServiceEntity service = serviceService.getObjectById(serviceId).get();

        Optional<Order> unConfirmedOrder = orderService.getUnConfirmedByCustomerId(customerId);
        if(unConfirmedOrder.isEmpty()){
            unConfirmedOrder = Optional.ofNullable(orderService.add(customerId));
        }
        throwExceptionIfServiceHasAlreadyExistedInOrder(serviceId,unConfirmedOrder.get().getId());

        ServicesOfOrders servicesOfOrders = new ServicesOfOrders();
        servicesOfOrders.setProgressStatus(ProgressStatus.UN_CONFIRMED);
        servicesOfOrders.setOrder(unConfirmedOrder.get());
        servicesOfOrders.setCustomer(customer);
        servicesOfOrders.setService(service);
        servicesOfOrders.setServiceCategory(service.getServiceCategory());
        servicesOfOrderRepo.save(servicesOfOrders);
        
        unConfirmedOrder.get().incrementRequiredTime(service.getRequiredTime());
        unConfirmedOrder.get().incrementTotalPrice(service.getPrice());
        orderService.update(unConfirmedOrder.get());
    }


    public void setWorkerServiceTask(Long customerId, Long serviceId, Worker worker) {
        // Find the ServicesOfOrders instance by serviceId and customerId
        Optional<ServicesOfOrders> servicesOfOrder = getObjectFirstByServiceIdAndCustomerId(serviceId, customerId);

        // Check if the ServicesOfOrders instance is present
        if (servicesOfOrder.isPresent()) {
            ServicesOfOrders servicesOfOrders = servicesOfOrder.get();

            // Set the worker
            servicesOfOrders.setWorker(worker);
            servicesOfOrders.setProgressStatus(ProgressStatus.ON_WORKING);
            servicesOfOrders.setServiceDate(new Date());

            // Save the updated ServicesOfOrders instance
            servicesOfOrderRepo.save(servicesOfOrders);
            workerService.updateWorkerStatus(worker.getId(), WorkerStatus.UN_AVAILABLE);
        } else {
            // Handle the case where the ServicesOfOrders instance is not found
            throw new RuntimeException("No service found for the given serviceId and customerId");
        }
    }

    @Override
    public void confirmAllServiceOfOrder(Long orderId) {
        servicesOfOrderRepo.confirmAllServiceOfOrder(orderId);
    }

    @Override
    public List<ServicesOfOrders> getObjectByOrderId(Long orderId) {
        return servicesOfOrderRepo.findObjectByOrderId(orderId);
    }

}
