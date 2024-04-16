package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.*;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserResponse;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.enums.ServiceCategory;
import com.GP.ELsayes.model.enums.roles.UserRole;
import com.GP.ELsayes.model.mapper.CustomerMapper;
import com.GP.ELsayes.model.mapper.UserMapper;
import com.GP.ELsayes.repository.CustomerRepo;
import com.GP.ELsayes.service.*;
import com.GP.ELsayes.service.relations.PackagesOfOrderService;
import com.GP.ELsayes.service.relations.ServicesOfOrderService;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

//@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements UserService, CustomerService {
    private final CustomerMapper customerMapper;
    private final CustomerRepo customerRepo;
    private final UserMapper userMapper;
    private final CarService carService;
    private OrderService orderService;
    private final ServiceService serviceService;
    private final FreeTrialCodeService freeTrialCodeService;
    private final ServicesOfOrderService servicesOfOrderService;
    private final PackagesOfOrderService packagesOfOrderService;


    public CustomerServiceImpl(CustomerMapper customerMapper, CustomerRepo customerRepo,
                               UserMapper userMapper, @Lazy CarService carService,
                               @Lazy OrderService orderService,
                               @Lazy ServiceService serviceService, @Lazy FreeTrialCodeService freeTrialCodeService,
                               @Lazy ServicesOfOrderService servicesOfOrderService,
                               @Lazy PackagesOfOrderService packagesOfOrderService) {
        this.customerMapper = customerMapper;
        this.customerRepo = customerRepo;
        this.userMapper = userMapper;
        this.carService = carService;
        this.orderService = orderService;
        this.serviceService = serviceService;
        this.freeTrialCodeService = freeTrialCodeService;
        this.servicesOfOrderService = servicesOfOrderService;
        this.packagesOfOrderService = packagesOfOrderService;
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

    @Override
    public CustomerResponse register(CustomerRequest customerRequest){
        return add(customerRequest);
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
    public Optional<Customer> getObjectById(Long customerId) {
        return customerRepo.findById(customerId);
    }

    @Override
    public Customer getById(Long customerId) {
        return getObjectById(customerId).orElseThrow(
                () -> new NoSuchElementException("There is no customer with id = " + customerId)
        );
    }

    @Override
    public CustomerResponse getResponseById(Long customerId) {
        return customerMapper.toResponse(getById(customerId));
    }


    @Override
    public CarResponse addCarToCustomer(AddCarRequest addCarRequest){
        return carService.addCarToCustomer(addCarRequest);
    }

    @Override
    public CarResponse updateCar(CarRequest carRequest, Long carId){
        return carService.update(carRequest,carId);
    }

    @Override
    public void deleteCra(Long carId){
        carService.delete(carId);
    }

    @Override
    public CarResponse getCarById(Long carId){
        return carService.getResponseById(carId);
    }


    @Override
    public List<ServiceResponse> getAllCleaningServices(){
        return  serviceService.getAllCleaningServices();
    }

    @Override
    public List<ServiceResponse> getAllMaintenanceServices(){
        return  serviceService.getAllMaintenanceServices();
    }

    @Override
    public List<ServiceResponse> getAllTakeAwayServices(){
        return  serviceService.getAllTakeAwayServices();
    }

    @Override
    public ServiceResponse getServiceByIdAndBranchId(Long serviceId, Long branchId){
        return serviceService.getResponseByServiceIdOrAndServiceIdBranchId(serviceId,branchId);
    }

    @Override
    public void addServiceToOrderList(AddServiceToOrderListRequest addServiceToOrderListRequest){
        servicesOfOrderService.addServiceToOrder(
                addServiceToOrderListRequest.getCustomerId(),
                addServiceToOrderListRequest.getServiceId()
        );
    }

    @Override
    public void deleteServiceFromOrderList(Long serviceOfOrderId){
        servicesOfOrderService.deleteServiceFromOrderList(serviceOfOrderId);
    }

    @Override
    public void addPackageToOrderList(AddPackageToOrderListRequest addPackageToOrderListRequest) {
        packagesOfOrderService.addPackageToOrder(
                addPackageToOrderListRequest.getCustomerId(),
                addPackageToOrderListRequest.getPackageId()
                );
    }

    @Override
    public void deletePackageFromOrderList(Long packageOfOrderId){
        packagesOfOrderService.deletePackageFromOrderList(packageOfOrderId);
    }

    @Override
    public void clearOrderList(Long customerId){
        orderService.clearOrderListByCustomerId(customerId);
    }

    @Override
    public void confirmOrder(Long customerId){
        orderService.confirmOrderByCustomerId(customerId);
    }

    @Override
    public OrderResponse getUnConfirmedOrder(Long customerId){
        return orderService.getResponseUnConfirmedByCustomerId(customerId);
    }

    @Override
    public OrderProgressResponse getProgressOfConfirmedOrder(Long customerId){
        return orderService.getProgressOfConfirmedOrderByCustomerId(customerId);
    }

}
