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
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class ServicesOfOrderServiceImpl implements ServicesOfOrderService {

    private final ServicesOfOrderRepo servicesOfOrderRepo;
    private final OrderService orderService;
    private final CustomerService customerService;
    private final ServiceService serviceService;
    private final WorkerService workerService;

    public ServicesOfOrderServiceImpl(ServicesOfOrderRepo servicesOfOrderRepo,
                                      OrderService orderService,
                                      CustomerService customerService,
                                      ServiceService serviceService,
                                      @Lazy WorkerService workerService) {
        this.servicesOfOrderRepo = servicesOfOrderRepo;
        this.orderService = orderService;
        this.customerService = customerService;
        this.serviceService = serviceService;
        this.workerService = workerService;
    }


    private void throwExceptionIfServiceHasAlreadyExistedInOrder(Long serviceId , Long orderId){
        Optional<ServicesOfOrders> servicesOfOrder = servicesOfOrderRepo.findByServiceIdAndOrderId(serviceId,orderId);
        if(servicesOfOrder.isEmpty()){
            return;
        }
        throw new RuntimeException("This service with id "+ serviceId +" already existed in this order");
    }

    private boolean orderIdFinished(Long orderId) {
        List<ServicesOfOrders> servicesOfOrder = servicesOfOrderRepo.findObjectByOrderId(orderId);
        return servicesOfOrder.stream().allMatch(serviceOrder -> serviceOrder.getServiceFinishDate() != null);
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
    public void finishServiceTask(Long customerId, Long workerId) {
        Optional<ServicesOfOrders> servicesOfOrder = servicesOfOrderRepo.findByCustomerIdAndWorkerId(customerId,workerId);
        // Check if the ServicesOfOrders instance is present
        if (servicesOfOrder.isPresent()) {

            ServicesOfOrders servicesOfOrders = servicesOfOrder.get();

            servicesOfOrders.setWorker(servicesOfOrders.getWorker());
            servicesOfOrders.setProgressStatus(ProgressStatus.FINISHED);
            servicesOfOrders.setServiceDate(servicesOfOrders.getServiceDate());
            servicesOfOrders.setServiceFinishDate(new Date());

            servicesOfOrderRepo.save(servicesOfOrders);

            if (orderIdFinished(servicesOfOrders.getOrder().getId()))
                orderService.endTheOrder(servicesOfOrders.getOrder().getId());

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

    public void deleteServiceFromOrderList(Long serviceId){
        ServicesOfOrders servicesOfOrder = servicesOfOrderRepo.findById(serviceId).orElseThrow(
                () -> new NoSuchElementException("There is no service with id = " + serviceId)
        );
        if (servicesOfOrder.getProgressStatus() != ProgressStatus.UN_CONFIRMED){
            throw new RuntimeException("Order is confirmed, you can not delete");
        }
        servicesOfOrderRepo.deleteById(serviceId);
    }

}
