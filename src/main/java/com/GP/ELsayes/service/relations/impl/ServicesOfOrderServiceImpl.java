package com.GP.ELsayes.service.relations.impl;

import com.GP.ELsayes.model.entity.Order;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.GP.ELsayes.model.entity.relations.PackagesOfOrder;
import com.GP.ELsayes.model.entity.relations.ServicesOfOrders;
import com.GP.ELsayes.model.enums.ProgressStatus;
import com.GP.ELsayes.model.enums.WorkerStatus;
import com.GP.ELsayes.repository.relations.ServicesOfOrderRepo;
import com.GP.ELsayes.service.*;
import com.GP.ELsayes.service.relations.PackagesOfOrderService;
import com.GP.ELsayes.service.relations.ServicesOfOrderService;
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
    private final PackagesOfOrderService packagesOfOrderService;




    public ServicesOfOrderServiceImpl(ServicesOfOrderRepo servicesOfOrderRepo,
                                      OrderService orderService,
                                      CustomerService customerService,
                                      ServiceService serviceService,
                                      @Lazy WorkerService workerService,
                                      @Lazy PackagesOfOrderService packagesOfOrderService){
        this.servicesOfOrderRepo = servicesOfOrderRepo;
        this.orderService = orderService;
        this.customerService = customerService;
        this.serviceService = serviceService;
        this.workerService = workerService;
        this.packagesOfOrderService = packagesOfOrderService;
    }


    private void throwExceptionIfServiceHasAlreadyExistedInOrder(Long serviceId , Long orderId){
        Optional<ServicesOfOrders> servicesOfOrder = servicesOfOrderRepo.findByServiceIdAndOrderId(serviceId,orderId);
        if(servicesOfOrder.isEmpty()){
            return;
        }
        throw new RuntimeException("This service with id "+ serviceId +" already existed in this order");
    }

    private boolean checkIfOrderIsFinished(Long orderId) {
        List<ServicesOfOrders> servicesOfOrder = servicesOfOrderRepo.findObjectByOrderId(orderId);
        return servicesOfOrder.stream().allMatch(serviceOrder -> serviceOrder.getFinishWorkingDate() != null);
    }

    private boolean checkIfAllServicesOfOrderPackageIsFinished(Long orderId, Long packageOfOrderId) {
        List<ServicesOfOrders> servicesOfOrder = servicesOfOrderRepo.findObjectByOrderIdAndPackageOfOrderId(orderId,packageOfOrderId);
        return servicesOfOrder.stream().allMatch(serviceOrder -> serviceOrder.getFinishWorkingDate() != null);
    }


    public Optional<ServicesOfOrders> getObjectFirstByServiceIdAndCustomerId(Long serviceId, Long customerId) {
        List<ServicesOfOrders> servicesOfOrders = servicesOfOrderRepo.findByServiceIdAndCustomerId(serviceId, customerId);
        if (!servicesOfOrders.isEmpty()) {
            return Optional.of(servicesOfOrders.get(0));
        }
        return Optional.empty();
    }



    @Override
    public void addServiceToOrder(Long customerId, Long serviceId ,Boolean inPackage) {
        Customer customer = customerService.getById(customerId);
        ServiceEntity service = serviceService.getById(serviceId);

        Order unConfirmedOrder = orderService.getUnConfirmedByCustomerId(customerId)
                .orElseGet(() -> orderService.add(customerId));

        throwExceptionIfServiceHasAlreadyExistedInOrder(serviceId, unConfirmedOrder.getId());

        ServicesOfOrders servicesOfOrder = new ServicesOfOrders();
        servicesOfOrder.setProgressStatus(ProgressStatus.UN_CONFIRMED);
        servicesOfOrder.setOrder(unConfirmedOrder);
        servicesOfOrder.setCustomer(customer);
        servicesOfOrder.setService(service);
        servicesOfOrder.setServiceCategory(service.getServiceCategory());

        if (inPackage == true)
            packagesOfOrderService.getByCustomerIdAndOrderId(customer.getId(), unConfirmedOrder.getId())
                    .ifPresent(servicesOfOrder::setPackagesOfOrder);

        servicesOfOrderRepo.save(servicesOfOrder);

        unConfirmedOrder.incrementRequiredTime(service.getRequiredTime());
        unConfirmedOrder.incrementTotalPrice(service.getPrice());
        orderService.update(unConfirmedOrder);
    }

    public void addServiceToOrder(Long customerId, Long serviceId ){
        addServiceToOrder(customerId,serviceId,false);
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
            servicesOfOrders.setStartWorkingDate(new Date());

            // Save the updated ServicesOfOrders instance
            servicesOfOrderRepo.save(servicesOfOrders);
            workerService.updateWorkerStatus(worker.getId(), WorkerStatus.UN_AVAILABLE);
            packagesOfOrderService.updateStatusOfAllOrderPackage(servicesOfOrder.get().getOrder().getId(),ProgressStatus.ON_WORKING);
        } else {
            // Handle the case where the ServicesOfOrders instance is not found
            throw new RuntimeException("No service found for the given serviceId and customerId");
        }
    }

    @Override
    public void finishServiceTask(Long customerId, Long workerId) {
        ServicesOfOrders servicesOfOrders = servicesOfOrderRepo.findByCustomerIdAndWorkerId(customerId, workerId)
                .orElseThrow(() -> new RuntimeException("Worker with id = " + workerId +
                        " does not work on any services for this car."));

        servicesOfOrders.setProgressStatus(ProgressStatus.FINISHED);
        servicesOfOrders.setFinishWorkingDate(new Date());
        servicesOfOrderRepo.save(servicesOfOrders);

        Long orderId = servicesOfOrders.getOrder().getId();
        if (checkIfOrderIsFinished(orderId)) {
            orderService.endTheOrder(orderId);
        }

        PackagesOfOrder packageOfOrder = servicesOfOrders.getPackagesOfOrder();
        if (packageOfOrder != null && checkIfAllServicesOfOrderPackageIsFinished(orderId, packageOfOrder.getId())) {
            packagesOfOrderService.updateStatusOfAllOrderPackage(orderId, ProgressStatus.FINISHED);
        }
    }

    @Override
    public List<ServicesOfOrders> getAllUnConfirmedByPackageOfOrderId(Long packageOfOrderId) {
        return servicesOfOrderRepo.findAllUnConfirmedByPackageOrderId(packageOfOrderId);
    }


    @Override
    public void confirmAllServiceOfOrder(Long orderId) {
        servicesOfOrderRepo.confirmAllServiceOfOrder(orderId);
    }

    @Override
    public List<ServicesOfOrders> getObjectByOrderId(Long orderId) {
        return servicesOfOrderRepo.findObjectByOrderId(orderId);
    }

    public void deleteServiceFromOrderList(Long serviceOfOrderId){
        ServicesOfOrders servicesOfOrder = servicesOfOrderRepo.findById(serviceOfOrderId).orElseThrow(
                () -> new NoSuchElementException("There is no service with id = " + serviceOfOrderId)
        );
        if (servicesOfOrder.getProgressStatus() != ProgressStatus.UN_CONFIRMED){
            throw new RuntimeException("Order is confirmed, you can not delete");
        }
        servicesOfOrderRepo.deleteById(serviceOfOrderId);
    }

}
