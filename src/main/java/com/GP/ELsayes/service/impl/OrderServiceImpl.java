package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.OrderResponse;
import com.GP.ELsayes.model.entity.Order;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.entity.relations.VisitationsOfBranches;
import com.GP.ELsayes.model.enums.ProgressStatus;
import com.GP.ELsayes.repository.OrderRepo;
import com.GP.ELsayes.service.CustomerService;
import com.GP.ELsayes.service.OrderHandlingService;
import com.GP.ELsayes.service.OrderService;
import com.GP.ELsayes.service.ServiceService;
import com.GP.ELsayes.service.relations.ServicesOfOrderService;
import com.GP.ELsayes.service.relations.VisitationsOfBranchesService;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final CustomerService customerService;
    private final ServiceService serviceService;
    private final ServicesOfOrderService servicesOfOrderService;
    private final VisitationsOfBranchesService visitationsOfBranchesService;
    private final OrderHandlingService orderHandlingService;

    public OrderServiceImpl(OrderRepo orderRepo,@Lazy CustomerService customerService,
                            @Lazy ServiceService serviceService,@Lazy ServicesOfOrderService servicesOfOrderService,
                            VisitationsOfBranchesService visitationsOfBranchesService,
                            @Lazy OrderHandlingService orderHandlingService) {
        this.orderRepo = orderRepo;
        this.customerService = customerService;
        this.serviceService = serviceService;
        this.servicesOfOrderService = servicesOfOrderService;
        this.visitationsOfBranchesService = visitationsOfBranchesService;
        this.orderHandlingService = orderHandlingService;
    }


    private void throwExceptionIfCustomerHasAnOrderNotFinishYet(Long customerId) {
        Optional<Order> unFinishedOrder = orderRepo.findUnFinishedByCustomerId(customerId);
        if(unFinishedOrder.isEmpty()){
            return;
        }
        throw new RuntimeException("Please,wait until your first order to be finish.");
    }

    private VisitationsOfBranches throwExceptionIfCustomerNotInAnyBranch(Long customerId) {
        Optional<VisitationsOfBranches> visitationsOfBranch = visitationsOfBranchesService.getCurrentVisitationByCustomerId(customerId);
        if(visitationsOfBranch.isEmpty()){
            throw new RuntimeException("You can't confirm your order until you arrive to any from our branches.");
        }
        return visitationsOfBranch.get();
    }

    @Override
    public Order add(Long customerId) {
        Customer customer = customerService.getById(customerId);

        Order order = new Order();
        order.setCustomer(customer);
        order.setProgressStatus(ProgressStatus.UN_CONFIRMED);

        return orderRepo.save(order);
    }

    @SneakyThrows
    @Override
    public Order update(Order order) {
        Order updatedOrder = order;
        Order existedOrder = getObjectById(order.getId()).get();

        updatedOrder.setId(existedOrder.getId());
        BeanUtils.copyProperties(existedOrder,updatedOrder);
        updatedOrder.setOrderFinishDate(order.getOrderFinishDate());

        return orderRepo.save(updatedOrder);
    }

    public void updateOrderStatus(Long orderId,ProgressStatus progressStatus){
        Optional<Order> order = getObjectById(orderId);
        order.get().setProgressStatus(progressStatus);
        update(order.get());
    }

    @Override
    public void endTheOrder(Long orderId) {
        Optional<Order> order = getObjectById(orderId);
        order.get().setProgressStatus(ProgressStatus.FINISHED);
        order.get().setOrderFinishDate(new Date());
        update(order.get());
    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public List<OrderResponse> getAll() {
        return null;
    }

    @Override
    public Optional<Order> getObjectById(Long orderId) {
        return orderRepo.findById(orderId);
    }

    @Override
    public Order getById(Long orderId) {
        return getObjectById(orderId).orElseThrow(
                () -> new NoSuchElementException("There is no order with id = " + orderId)
        );
    }

    @Override
    public OrderResponse getResponseById(Long orderId) {
       //return offerMapper.toResponse(getById(orderId);
        return null;
    }
    
    @Override
    public Optional<Order> getUnConfirmedByCustomerId(Long customerId) {
        return orderRepo.findUnConfirmedByCustomerId(customerId);
    }


    public List<ServiceEntity> findServicesNotInBranch(Long orderId,Long branchId){

        List<ServiceEntity> servicesOfOrder =  serviceService.getAllByOrderId(orderId);
        List<ServiceEntity> servicesOfBranch = serviceService.getAllAvailableInBranch(branchId);

        // List to hold services not included in servicesOfBranch
        List<ServiceEntity> servicesNotInBranch = new ArrayList<>();

        // Iterate through servicesOfOrder and check if they are contained in servicesOfBranch
        for (ServiceEntity orderService : servicesOfOrder) {
            if (!servicesOfBranch.contains(orderService)) {
                servicesNotInBranch.add(orderService);
            }
        }

        // Return the list of services not included in servicesOfBranch
        return servicesNotInBranch;
    }

    @Override
    public void confirmOrderByCustomerId(Long customerId) {
        customerService.getById(customerId);
        VisitationsOfBranches visitationsOfBranch = throwExceptionIfCustomerNotInAnyBranch(customerId);
        throwExceptionIfCustomerHasAnOrderNotFinishYet(customerId);

        Order unConfirmedOrder = getUnConfirmedByCustomerId(customerId).orElseThrow(
                () -> new NoSuchElementException("Order list is empty,add services to list.")
        );

        List<ServiceEntity> servicesNotInBranch = findServicesNotInBranch(
                unConfirmedOrder.getId(),
                visitationsOfBranch.getBranch().getId()
        );

        if (!servicesNotInBranch.isEmpty()){
            throw new RuntimeException("some services is not available in this branch :" +
                    servicesNotInBranch.stream()
                    .map(service ->  service.getId())
                    .toList()
                    );
        }

        unConfirmedOrder.setOrderDate(new Date());
        unConfirmedOrder.setProgressStatus(ProgressStatus.WAITING);
        unConfirmedOrder.setBranch(visitationsOfBranch.getBranch());
        unConfirmedOrder = update(unConfirmedOrder);
        servicesOfOrderService.confirmAllServiceOfOrder(unConfirmedOrder.getId());

        orderHandlingService.saveOrder(unConfirmedOrder.getId());

    }


}
