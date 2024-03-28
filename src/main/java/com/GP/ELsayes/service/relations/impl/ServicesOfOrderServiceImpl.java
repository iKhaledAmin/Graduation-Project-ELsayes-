package com.GP.ELsayes.service.relations.impl;

import com.GP.ELsayes.model.entity.Order;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.entity.relations.ServicesOfOrders;
import com.GP.ELsayes.model.enums.ProgressStatus;
import com.GP.ELsayes.repository.relations.ServicesOfOrderRepo;
import com.GP.ELsayes.service.CustomerService;
import com.GP.ELsayes.service.OrderService;
import com.GP.ELsayes.service.ServiceService;
import com.GP.ELsayes.service.relations.ServicesOfOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ServicesOfOrderServiceImpl implements ServicesOfOrderService {

    private final ServicesOfOrderRepo servicesOfOrderRepo;
    private final OrderService orderService;
    private final CustomerService customerService;
    private final ServiceService serviceService;

    private void throwExceptionIfServiceHasAlreadyExistedInOrder(Long serviceId , Long orderId){
        Optional<ServicesOfOrders> servicesOfOrder = servicesOfOrderRepo.findByServiceIdAndOrderId(serviceId,orderId);
        if(servicesOfOrder.isEmpty()){
            return;
        }
        throw new RuntimeException("This service with id "+ serviceId +" already existed in this order");
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
        servicesOfOrderRepo.save(servicesOfOrders);
        
        unConfirmedOrder.get().incrementRequiredTime(service.getRequiredTime());
        unConfirmedOrder.get().incrementTotalPrice(service.getPrice());
        orderService.update(unConfirmedOrder.get());
    }

    @Override
    public void confirmAllServiceOfOrder(Long orderId) {
        servicesOfOrderRepo.confirmAllServiceOfOrder(orderId);
    }

}
