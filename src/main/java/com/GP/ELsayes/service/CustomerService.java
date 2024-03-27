package com.GP.ELsayes.service;

import com.GP.ELsayes.model.dto.AddCarToCustomerRequest;
import com.GP.ELsayes.model.dto.CarResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerResponse;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService extends  UserService, CrudService<CustomerRequest, Customer, CustomerResponse,Long> {
    public CarResponse addCarToCustomer(AddCarToCustomerRequest addCarToCustomerRequest);
}
