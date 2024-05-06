package com.GP.ELsayes.service;


import com.GP.ELsayes.model.dto.OrderProgressResponse;
import com.GP.ELsayes.model.dto.OrderResponse;
import com.GP.ELsayes.model.dto.ServicesOfOrderResponse;
import com.GP.ELsayes.model.entity.Order;
import com.GP.ELsayes.model.enums.ProgressStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface OrderService {
    public Order add(Long customerId) ;
    public Order update(Order updatedOrder);
    public Optional<Order> getObjectById(Long orderId);
    public Order getById(Long orderId);
    public Optional<Order> getUnConfirmedByCustomerId(Long customerId);
    public OrderResponse getResponseUnConfirmedByCustomerId(Long customerId,Long branchId);
    public OrderProgressResponse getProgressOfConfirmedOrderByCustomerId(Long customerId);

    Optional<Order> getFinishedByCustomerId(Long customerId);
    public OrderResponse getResponseFinishedOrderByCustomerId(Long customerId);
    Optional<Order> getUnFinishedOrderByCustomerId(Long customerId);

    public void confirmOrderByCustomerId(Long customerId);
    public void updateOrderStatus(Long orderId, ProgressStatus progressStatus);
    public void endTheOrder(Long orderId);

    void clearOrderListByCustomerId(Long customerId);
}
