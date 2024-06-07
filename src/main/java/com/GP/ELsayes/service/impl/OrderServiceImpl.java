package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.OrderProgressResponse;
import com.GP.ELsayes.model.dto.OrderResponse;
import com.GP.ELsayes.model.dto.PackagesOfOrderResponse;
import com.GP.ELsayes.model.dto.ServicesOfOrderResponse;
import com.GP.ELsayes.model.entity.Order;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.entity.relations.PackagesOfOrder;
import com.GP.ELsayes.model.entity.relations.ServicesOfOrders;
import com.GP.ELsayes.model.entity.relations.VisitationsOfBranches;
import com.GP.ELsayes.model.enums.ProgressStatus;
import com.GP.ELsayes.repository.OrderRepo;
import com.GP.ELsayes.service.CustomerService;
import com.GP.ELsayes.service.OrderHandlingService;
import com.GP.ELsayes.service.OrderService;
import com.GP.ELsayes.service.ServiceService;
import com.GP.ELsayes.service.relations.PackagesOfOrderService;
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
    private final PackagesOfOrderService packagesOfOrderService;
    private final VisitationsOfBranchesService visitationsOfBranchesService;
    private final OrderHandlingService orderHandlingService;

    public OrderServiceImpl(OrderRepo orderRepo, @Lazy CustomerService customerService,
                            @Lazy ServiceService serviceService, @Lazy ServicesOfOrderService servicesOfOrderService,
                            @Lazy PackagesOfOrderService packagesOfOrderService,
                            VisitationsOfBranchesService visitationsOfBranchesService,
                            @Lazy OrderHandlingService orderHandlingService) {
        this.orderRepo = orderRepo;
        this.customerService = customerService;
        this.serviceService = serviceService;
        this.servicesOfOrderService = servicesOfOrderService;
        this.packagesOfOrderService = packagesOfOrderService;
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
    public void clearOrderListByCustomerId(Long customerId) {
        Optional<Order> unConfirmed = Optional.ofNullable(getUnConfirmedByCustomerId(customerId).orElseThrow(
                () -> new RuntimeException("Order list is already empty!")
        ));
        orderRepo.deleteById(unConfirmed.get().getId());
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
    public OrderResponse getResponseById(Long orderId){
        OrderResponse orderResponse = new OrderResponse();
        Order order = getById(orderId);

        orderResponse.setId(orderId);
        orderResponse.setOrderDate(order.getOrderDate());
        orderResponse.setOrderFinishDate(order.getOrderFinishDate());
        orderResponse.setTotalRequiredTime(order.getTotalRequiredTime());
        orderResponse.setTotalPrice(order.getTotalPrice());
        orderResponse.setProgressStatus(order.getProgressStatus());
        orderResponse.setServices(servicesOfOrderService.getResponseAllByOrderId(orderId));
        orderResponse.setPackages(packagesOfOrderService.getResponseAllByOrderId(orderId));
        orderResponse.setOrderTotalCost(order.getTotalPrice());

        orderResponse.setCustomerName(order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName());

        return orderResponse;
    }

    List<Order> getAll(){
        return orderRepo.findAll();
    }

    List<Order> getAllByByBranchId(Long branchId){
        return orderRepo.findAllByBranchId(branchId);
    }

    @Override
    public List<OrderResponse> getAllResponseByBranchId(Long branchId){
        List<Order> orders = getAllByByBranchId(branchId);
        List<OrderResponse> responseList = new ArrayList<>();
        orders.forEach(order -> {
            responseList.add(getResponseById(order.getId()));
        });

        return responseList;
    }

    @Override
    public Optional<Order> getUnConfirmedByCustomerId(Long customerId) {
        return orderRepo.findUnConfirmedByCustomerId(customerId);
    }


    @Override
    public Optional<Order> getUnFinishedOrderByCustomerId(Long customerId) {
        return orderRepo.findUnFinishedByCustomerId(customerId);
    }

    @Override
    public OrderResponse getResponseUnConfirmedByCustomerId(Long customerId,Long branchId) {
        customerService.getById(customerId);
        Optional<Order> unConfirmedOrder = orderRepo.findUnConfirmedByCustomerId(customerId);

        if (unConfirmedOrder.isEmpty())
            return null;

        List<ServicesOfOrderResponse> servicesOfOrder = servicesOfOrderService.getUnConfirmedServicesOfOrderByCustomerId(customerId,branchId);
        List<PackagesOfOrderResponse> packagesOfOrder = packagesOfOrderService.getUnConfirmedPackagesOfOrderByCustomerId(customerId,branchId);

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setServices(servicesOfOrder);
        orderResponse.setPackages(packagesOfOrder);
        orderResponse.setOrderTotalCost(unConfirmedOrder.get().getTotalPrice());
        orderResponse.setTotalRequiredTime(unConfirmedOrder.get().getTotalRequiredTime());
//        return new OrderResponse(
//                servicesOfOrder
//                ,packagesOfOrder
//                ,unConfirmedOrder.get().getTotalPrice()
//                ,unConfirmedOrder.get().getTotalRequiredTime()
//        );

        return orderResponse;
    }


    @Override
    public OrderResponse getResponseFinishedOrderByCustomerId(Long customerId){
        Optional<Order> finishedOrder = getFinishedByCustomerId(customerId);
        List<ServicesOfOrderResponse> servicesOfOrder = servicesOfOrderService.getResponseAllByOrderId(finishedOrder.get().getId());
        List<PackagesOfOrderResponse> packagesOfOrder = packagesOfOrderService.getResponseAllByOrderId(finishedOrder.get().getId());

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setServices(servicesOfOrder);
        orderResponse.setPackages(packagesOfOrder);
        orderResponse.setOrderTotalCost(finishedOrder.get().getTotalPrice());
        orderResponse.setTotalRequiredTime(finishedOrder.get().getTotalRequiredTime());
//        return new OrderResponse(
//                servicesOfOrder
//                ,packagesOfOrder
//                ,finishedOrder.get().getTotalPrice()
//                ,finishedOrder.get().getTotalRequiredTime()
//        );

        return orderResponse;
    }

    @Override
    public Optional<Order> getFinishedByCustomerId(Long customerId) {
        return orderRepo.findFinishedByCustomerId(customerId);
    }

    @Override
    public  OrderProgressResponse getProgressOfConfirmedOrderByCustomerId(Long customerId){
        List<ServicesOfOrderResponse> servicesOfOrder = servicesOfOrderService.getResponseAllConfirmedByCustomerId(customerId);
        Optional<Order> confirmedOrder = orderRepo.findConfirmedByCustomerId(customerId);

        if (confirmedOrder.isEmpty())
            return null;

        return new  OrderProgressResponse(
                servicesOfOrder,
                confirmedOrder.get().getProgressStatus()
        );
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
        packagesOfOrderService.confirmAllPackagesOfOrder(unConfirmedOrder.getId());


        orderHandlingService.saveOrder(unConfirmedOrder.getId());

        visitationsOfBranch.setOrder(unConfirmedOrder);
        visitationsOfBranchesService.update(visitationsOfBranch);

    }


}
