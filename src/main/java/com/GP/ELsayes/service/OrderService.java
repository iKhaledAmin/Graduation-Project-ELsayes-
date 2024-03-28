package com.GP.ELsayes.service;


import com.GP.ELsayes.model.dto.OrderResponse;
import com.GP.ELsayes.model.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface OrderService {
    public Order add(Long customerId) ;

    public Order update(Order updatedOrder);

    public void delete(Long aLong);


    public List<OrderResponse> getAll();

    Optional<Order> getObjectById(Long orderId);

    public Order getById(Long orderId);

    public OrderResponse getResponseById(Long orderId) ;

    public Optional<Order> getUnConfirmedByCustomerId(Long customerId);

    //public Optional<Order> getLastConfirmedByCustomerId(Long customerId);

    //public Optional<Order> getUnConfirmedByServiceId(Long serviceId);

    public void confirmOrderByCustomerId(Long customerId);



}
