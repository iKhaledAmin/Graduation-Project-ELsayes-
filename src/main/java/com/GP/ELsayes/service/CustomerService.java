package com.GP.ELsayes.service;

import com.GP.ELsayes.model.dto.*;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerResponse;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService extends  UserService, CrudService<CustomerRequest, Customer, CustomerResponse,Long> {
    public CustomerResponse register(CustomerRequest customerRequest);

    public CarResponse addCarToCustomer(AddCarRequest addCarRequest);
    public CarResponse updateCar(CarRequest carRequest, Long carId);
    public void deleteCra(Long carId);

    public CarResponse getCarById(Long carId);
    public void addServiceToOrderList(AddServiceToOrderListRequest addServiceToOrderListRequest);
    public void deleteServiceFromOrderList(Long serviceId);
    public void addPackageToOrderList(AddPackageToOrderListRequest addPackageToOrderListRequest);
    public void deletePackageFromOrderList(Long packageOfOrderId);
    public void clearOrderList(Long customerId);
    public void confirmOrder(Long customerId);
    public OrderResponse getUnConfirmedOrder(Long customerId);
    public OrderProgressResponse getProgressOfConfirmedOrder(Long customerId);

}
