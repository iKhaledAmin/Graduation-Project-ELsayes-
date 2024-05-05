package com.GP.ELsayes.service;

import com.GP.ELsayes.model.dto.*;
import com.GP.ELsayes.model.dto.SystemUsers.User.EditUserProfileRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserResponse;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.entity.relations.VisitationsOfBranches;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CustomerService extends CrudService<CustomerRequest, Customer, CustomerResponse,Long> {
    public CustomerResponse register(CustomerRequest customerRequest);
    public UserResponse editProfile(EditUserProfileRequest profileRequest, Long userId);

    public CarResponse addCarToCustomer(AddCarRequest addCarRequest);
    public CarResponse updateCar(CarRequest carRequest, Long carId);
    public void deleteCar(Long carId);

    public CarResponse getCarById(Long carId);
    public CarResponse getCarByCustomerId(Long customerId);

    public List<ServiceResponse> getAllCleaningServices();

    public List<ServiceResponse> getAllMaintenanceServices();

    public List<ServiceResponse> getAllTakeAwayServices();

    public ServiceResponse getServiceByIdAndBranchId(Long serviceId, Long branchId);

    public void addServiceToOrderList(AddServiceToOrderListRequest addServiceToOrderListRequest);
    public void deleteServiceFromOrderList(Long serviceId);
    public PackageResponse getPackageByIdAndBranchId(Long packageId, Long branchId);
    public List<PackageResponse> getAllPackages();
    public void addPackageToOrderList(AddPackageToOrderListRequest addPackageToOrderListRequest);
    public void deletePackageFromOrderList(Long packageOfOrderId);
    public void clearOrderList(Long customerId);
    public void confirmOrder(Long customerId);
    public OrderResponse getUnConfirmedOrder(Long customerId);
    public OrderProgressResponse getProgressOfConfirmedOrder(Long customerId);

    public Optional<VisitationsOfBranches> getVisitationsOfBranchByCustomerId(Long customerId);

}
