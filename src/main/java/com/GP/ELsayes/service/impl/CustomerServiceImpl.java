package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.*;
import com.GP.ELsayes.model.dto.SystemUsers.User.EditUserProfileRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserResponse;
import com.GP.ELsayes.model.entity.SystemUsers.User;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.entity.relations.VisitationsOfBranches;
import com.GP.ELsayes.model.enums.roles.UserRole;
import com.GP.ELsayes.model.mapper.CustomerMapper;
import com.GP.ELsayes.model.mapper.UserMapper;
import com.GP.ELsayes.repository.CustomerRepo;
import com.GP.ELsayes.repository.UserRepo;
import com.GP.ELsayes.service.*;
import com.GP.ELsayes.service.relations.PackagesOfOrderService;
import com.GP.ELsayes.service.relations.ServicesOfOrderService;
import com.GP.ELsayes.service.relations.VisitationsOfBranchesService;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

//@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerMapper customerMapper;
    private final CustomerRepo customerRepo;
    private final UserService userService;
    private final UserMapper userMapper;
    private final CarService carService;
    private OrderService orderService;
    private final ServiceService serviceService;

    private final PackageService packageService;
    private final FreeTrialCodeService freeTrialCodeService;
    private final ServicesOfOrderService servicesOfOrderService;
    private final PackagesOfOrderService packagesOfOrderService;
    private final VisitationsOfBranchesService visitationsOfBranchesService;


    public CustomerServiceImpl(CustomerMapper customerMapper, CustomerRepo customerRepo,
                               UserRepo userRepo, UserService userService, UserMapper userMapper, @Lazy CarService carService,
                               @Lazy OrderService orderService,
                               @Lazy ServiceService serviceService, @Lazy PackageService packageService,
                               @Lazy FreeTrialCodeService freeTrialCodeService,
                               @Lazy ServicesOfOrderService servicesOfOrderService,
                               @Lazy PackagesOfOrderService packagesOfOrderService,
                               @Lazy VisitationsOfBranchesService visitationsOfBranchesService) {
        this.customerMapper = customerMapper;
        this.customerRepo = customerRepo;
        this.userService = userService;
        this.userMapper = userMapper;
        this.carService = carService;
        this.orderService = orderService;
        this.serviceService = serviceService;
        this.packageService = packageService;
        this.freeTrialCodeService = freeTrialCodeService;
        this.servicesOfOrderService = servicesOfOrderService;
        this.packagesOfOrderService = packagesOfOrderService;
        this.visitationsOfBranchesService = visitationsOfBranchesService;
    }

    private void throwExceptionIfUserNameAlreadyExist(String userName) {
        Optional<User> user = userService.getEntityByUserName(userName);
        if(user.isPresent())
            throw new RuntimeException("User name already exist");
    }

    private void throwExceptionIfCustomerStillInBranch(Long customerId) {
        Customer customer = getById(customerId);
        Optional<VisitationsOfBranches> VisitationsOfBranches = getVisitationsOfBranchByCustomerId(customerId);
        if(VisitationsOfBranches.isPresent())
            throw new RuntimeException("Customer with id = " +customerId+ " still in branch ,can't delete");
    }



    @Override
    public CustomerResponse add(CustomerRequest customerRequest) {
        throwExceptionIfUserNameAlreadyExist(customerRequest.getUserName());

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
        updatedCustomer.setFirstName(customerRequest.getFirstName());
        updatedCustomer.setLastName(customerRequest.getLastName());
        updatedCustomer.setUserName(existedCustomer.getUserName());
        updatedCustomer.setPassword(customerRequest.getPassword());
        updatedCustomer.setEmail(customerRequest.getEmail());
        updatedCustomer.setImage(customerRequest.getImage());
        updatedCustomer.setPhoneNumber(customerRequest.getPhoneNumber());
        updatedCustomer.setBirthday(customerRequest.getBirthday());
        updatedCustomer.setGender(customerRequest.getGender());
        updatedCustomer.setDateOfJoining(existedCustomer.getDateOfJoining());
        updatedCustomer.setCustomerFreeTrialCode(existedCustomer.getCustomerFreeTrialCode());
        updatedCustomer.setUserRole(existedCustomer.getUserRole());

        System.out.println(updatedCustomer.getUserRole());

        return this.customerMapper.toResponse(customerRepo.save(updatedCustomer));
    }



    @Override
    public UserResponse editProfile(EditUserProfileRequest profileRequest, Long userId) {
        getById(userId);
        return userService.editProfile(profileRequest,userId);
    }



    @Override
    public void delete(Long customerId) {
        throwExceptionIfCustomerStillInBranch(customerId);
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
        return customerMapper.toResponseAccordingToBranch(getById(customerId),visitationsOfBranchesService);
    }

    @Override
    public Optional<VisitationsOfBranches> getVisitationsOfBranchByCustomerId(Long customerId){
        return visitationsOfBranchesService.getCurrentVisitationByCustomerId(customerId);
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
    public void deleteCar(Long carId){
        carService.delete(carId);
    }

    @Override
    public CarResponse getCarById(Long carId){
        return carService.getResponseById(carId);
    }

    @Override
    public CarResponse getCarByCustomerId(Long customerId){
        return carService.getResponseByCustomerId(customerId);
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
        return serviceService.getByServiceIdOrByServiceIdAndBranchId(serviceId,branchId);
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
    public PackageResponse getPackageByIdAndBranchId(Long packageId, Long branchId){
        return packageService.getByPackageIdOrByPackageIdAndBranchId(packageId,branchId);
    }

    @Override
    public List<PackageResponse> getAllPackages(){
        return packageService.getAll();
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
    public OrderResponse getUnConfirmedOrder(Long customerId,Long branchId){
        return orderService.getResponseUnConfirmedByCustomerId(customerId,branchId);
    }

    @Override
    public OrderProgressResponse getProgressOfConfirmedOrder(Long customerId){
        return orderService.getProgressOfConfirmedOrderByCustomerId(customerId);
    }

}
