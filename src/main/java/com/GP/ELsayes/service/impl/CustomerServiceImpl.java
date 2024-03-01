package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerResponse;
import com.GP.ELsayes.model.entity.FreeTrialCode;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.enums.roles.UserRole;
import com.GP.ELsayes.model.mapper.CustomerMapper;
import com.GP.ELsayes.repository.CustomerRepo;
import com.GP.ELsayes.service.CustomerService;
import com.GP.ELsayes.service.FreeTrialCodeService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

//@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerMapper customerMapper;
    private final CustomerRepo customerRepo;
    private final FreeTrialCodeService freeTrialCodeService;


    public CustomerServiceImpl( CustomerMapper customerMapper, CustomerRepo customerRepo,@Lazy FreeTrialCodeService freeTrialCodeService) {
        this.customerMapper = customerMapper;
        this.customerRepo = customerRepo;
        this.freeTrialCodeService = freeTrialCodeService;
    }



    @Override
    public CustomerResponse add(CustomerRequest customerRequest) {

        Customer customer = this.customerMapper.toEntity(customerRequest);
        customer.setDateOfJoining(new Date());
        customer.setUserRole(UserRole.CUSTOMER);
        customer = this.customerRepo.save(customer);

        if(customerRequest.getFreeTrialCode() != "")
            freeTrialCodeService.applyCode(customer.getId(),customerRequest.getFreeTrialCode());


        return this.customerMapper.toResponse(customer);
    }

    @SneakyThrows
    @Override
    public CustomerResponse update(CustomerRequest customerRequest, Long customerId) {

        Customer existedCustomer = this.getById(customerId);
        Customer updatedCustomer = this.customerMapper.toEntity(customerRequest);


        updatedCustomer.setId(customerId);
        updatedCustomer.setUserRole(existedCustomer.getUserRole());
        updatedCustomer.setDateOfJoining(existedCustomer.getDateOfJoining());
        BeanUtils.copyProperties(existedCustomer,updatedCustomer);

        return this.customerMapper.toResponse(customerRepo.save(existedCustomer));
    }

    @Override
    public void delete(Long customerId) {

        getById(customerId);
        customerRepo.deleteById(customerId);
    }

    @Override
    public List<CustomerResponse> getAll() {
        return customerRepo.findAll()
                .stream()
                .map(customer ->  customerMapper.toResponse(customer))
                .toList();
    }

    @Override
    public Customer getById(Long customerId) {
        return customerRepo.findById(customerId).orElseThrow(
                () -> new NoSuchElementException("There is no customer with id = " + customerId)
        );
    }

    @Override
    public CustomerResponse getResponseById(Long customerId) {
        return customerMapper.toResponse(getById(customerId));
    }
}
