package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.AddCarToCustomerRequest;
import com.GP.ELsayes.model.dto.CarResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserResponse;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.enums.roles.UserRole;
import com.GP.ELsayes.model.mapper.CustomerMapper;
import com.GP.ELsayes.model.mapper.UserMapper;
import com.GP.ELsayes.repository.CustomerRepo;
import com.GP.ELsayes.service.CarService;
import com.GP.ELsayes.service.CustomerService;
import com.GP.ELsayes.service.FreeTrialCodeService;
import com.GP.ELsayes.service.UserService;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

//@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements UserService, CustomerService {
    private final CustomerMapper customerMapper;
    private final CustomerRepo customerRepo;
    private final UserMapper userMapper;
    private final CarService carService;

    private final FreeTrialCodeService freeTrialCodeService;


    public CustomerServiceImpl(CustomerMapper customerMapper, CustomerRepo customerRepo, UserMapper userMapper,@Lazy CarService carService, @Lazy FreeTrialCodeService freeTrialCodeService) {
        this.customerMapper = customerMapper;
        this.customerRepo = customerRepo;
        this.userMapper = userMapper;
        this.carService = carService;
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
        BeanUtils.copyProperties(updatedCustomer,existedCustomer);

        return this.customerMapper.toResponse(customerRepo.save(existedCustomer));
    }

    @Override
    public UserResponse editProfile(UserRequest userRequest, Long userId) {
        Customer customer = getById(userId);

        CustomerRequest customerRequest = userMapper.toCustomerRequest(userRequest);


        CustomerResponse customerResponse = update(customerRequest,userId);

        return userMapper.toUserResponse(customerResponse);
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

    @Override
    public CarResponse addCarToCustomer(AddCarToCustomerRequest addCarToCustomerRequest){
        return carService.addCarToCustomer(addCarToCustomerRequest);
    }

}
