package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.OrderResponse;
import com.GP.ELsayes.model.entity.Order;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.entity.relations.VisitationsOfBranches;
import com.GP.ELsayes.model.enums.ProgressStatus;
import com.GP.ELsayes.repository.OrderRepo;
import com.GP.ELsayes.service.CustomerService;
import com.GP.ELsayes.service.OrderService;
import com.GP.ELsayes.service.relations.ServicesOfOrderService;
import com.GP.ELsayes.service.relations.VisitationsOfBranchesService;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final CustomerService customerService;
    private final ServicesOfOrderService servicesOfOrderService;
    private final VisitationsOfBranchesService visitationsOfBranchesService;

    public OrderServiceImpl(OrderRepo orderRepo, CustomerService customerService,
                            @Lazy ServicesOfOrderService servicesOfOrderService,
                            VisitationsOfBranchesService visitationsOfBranchesService) {
        this.orderRepo = orderRepo;
        this.customerService = customerService;
        this.servicesOfOrderService = servicesOfOrderService;
        this.visitationsOfBranchesService = visitationsOfBranchesService;
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

        return orderRepo.save(updatedOrder);
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



    @Override
    public void confirmOrderByCustomerId(Long customerId) {
        VisitationsOfBranches visitationsOfBranch = throwExceptionIfCustomerNotInAnyBranch(customerId);
        throwExceptionIfCustomerHasAnOrderNotFinishYet(customerId);

        Order unConfirmedOrder = getUnConfirmedByCustomerId(customerId).orElseThrow(
                () -> new NoSuchElementException("Order list is empty,add services to list.")
        );

        unConfirmedOrder.setDateOfOrder(new Date());
        unConfirmedOrder.setProgressStatus(ProgressStatus.WAITING);
        unConfirmedOrder.setBranch(visitationsOfBranch.getBranch());
        update(unConfirmedOrder);
        servicesOfOrderService.confirmAllServiceOfOrder(unConfirmedOrder.getId());
    }


}
