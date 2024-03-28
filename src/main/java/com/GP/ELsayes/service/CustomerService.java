package com.GP.ELsayes.service;

import com.GP.ELsayes.model.dto.AddCarRequest;
import com.GP.ELsayes.model.dto.AddServiceToOrderListRequest;
import com.GP.ELsayes.model.dto.CarResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerResponse;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService extends  UserService, CrudService<CustomerRequest, Customer, CustomerResponse,Long> {
    public CarResponse addCarToCustomer(AddCarRequest addCarRequest);
    public void addServiceToOrderList(AddServiceToOrderListRequest addServiceToOrderListRequest);
    public void confirmOrder(Long customerId);
}
