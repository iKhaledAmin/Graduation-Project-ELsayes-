package com.GP.ELsayes.service;

import com.GP.ELsayes.model.dto.*;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerResponse;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService extends  UserService, CrudService<CustomerRequest, Customer, CustomerResponse,Long> {
    public CarResponse addCarToCustomer(AddCarRequest addCarRequest);
    public void addServiceToOrderList(AddServiceToOrderListRequest addServiceToOrderListRequest);
    public void deleteServiceFromOrderList(Long serviceId);
    public void addPackageToOrderList(AddPackageToOrderListRequest addPackageToOrderListRequest);
    public void deletePackageFromOrderList(Long packageOfOrderId);
    public void confirmOrder(Long customerId);
    public OrderResponse getUnConfirmedOrder(Long customerId);
    public List<ServicesOfOrderResponse> getConfirmedOrder(Long customerId);

}
