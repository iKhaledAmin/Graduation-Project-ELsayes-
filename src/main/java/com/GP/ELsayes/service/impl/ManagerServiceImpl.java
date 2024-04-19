package com.GP.ELsayes.service.impl;
import com.GP.ELsayes.model.dto.PackageRequest;
import com.GP.ELsayes.model.dto.PackageResponse;
import com.GP.ELsayes.model.dto.ServiceRequest;
import com.GP.ELsayes.model.dto.ServiceResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.ManagerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.ManagerResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.WorkerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.WorkerResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserResponse;
import com.GP.ELsayes.model.dto.relations.*;
import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.entity.relations.OwnersOfManagers;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
import com.GP.ELsayes.model.enums.OperationType;
import com.GP.ELsayes.model.enums.permissions.ManagerPermission;
import com.GP.ELsayes.model.enums.roles.UserRole;
import com.GP.ELsayes.model.mapper.ManagerMapper;
import com.GP.ELsayes.model.mapper.UserMapper;
import com.GP.ELsayes.repository.ManagerRepo;
import com.GP.ELsayes.service.*;
import com.GP.ELsayes.service.relations.OwnersOfManagersService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;



@Service
public class ManagerServiceImpl implements UserService, ManagerService {
    private final ManagerMapper managerMapper;
    private final ManagerRepo managerRepo;
    private final UserMapper userMapper;
    private final BranchService branchService;
    private final OwnerService ownerService;
    private final ServiceService serviceService;
    private final WorkerService workerService;
    private final PackageService packageService;
    private final CustomerService customerService;
    private final OwnersOfManagersService ownersOfManagersService;

    public ManagerServiceImpl(ManagerMapper managerMapper, ManagerRepo managerRepo, UserMapper userMapper,
                              @Lazy BranchService branchService, @Lazy OwnerService ownerService,
                              @Lazy ServiceService serviceService, @Lazy WorkerService workerService,
                              @Lazy PackageService packageService, CustomerService customerService, OwnersOfManagersService ownersOfManagersService) {
        this.managerMapper = managerMapper;
        this.managerRepo = managerRepo;
        this.userMapper = userMapper;
        this.branchService = branchService;
        this.ownerService = ownerService;
        this.serviceService = serviceService;
        this.workerService = workerService;
        this.packageService = packageService;
        this.customerService = customerService;
        this.ownersOfManagersService = ownersOfManagersService;
    }




    private void throwExceptionIfBranchAlreadyHasAManager(Branch branch){
        if(branch.getManager() == null)
            return;
        throw new RuntimeException("This branch with id = "+ branch.getId() +" already have a manager and every branch only have one manager");
    }

    private void throwExceptionIfBranchHasAdifferentManager(Branch branch , Long managerId){

        if(branch.getManager() == null || branch.getManager().getId() == managerId)
            return;
        //throwExceptionIfBranchAlreadyHasAManager(branch);
        throw new RuntimeException("This branch with id = "+ branch.getId() +" already have a manager and every branch only have one manager");
    }

    @Override
    public Optional<Manager> getIfExistByBranchId(Long managerId) {
        return managerRepo.findByBranchId(managerId);
    }
    @Override
    public ManagerResponse add(ManagerRequest managerRequest) {

        Manager manager = this.managerMapper.toEntity(managerRequest);
        manager.setBranch(branchService.getById(managerRequest.getBranchId()));
        manager.setDateOfEmployment(new Date());
        manager.setTotalSalary(emp -> {
            double baseSalary = Double.parseDouble(emp.getBaseSalary());
            double bonus = Double.parseDouble(emp.getBonus());
            return baseSalary + bonus;
        });
        if (managerRequest.getManagerPermission() == ManagerPermission.FIRST_LEVEL)
            manager.setUserRole(UserRole.TOP_MANAGER);
        else manager.setUserRole(UserRole.MANAGER);



        Branch branch = branchService.getById(managerRequest.getBranchId());
        throwExceptionIfBranchAlreadyHasAManager(branch);

        manager = this.managerRepo.save(manager);


        Owner owner = ownerService.getById(managerRequest.getOwnerId());
        OwnersOfManagers ownersOfManagers = ownersOfManagersService.add(
                owner,
                manager,
                OperationType.CREATE
        );


        return this.managerMapper.toResponse(manager);
    }
    @Override
    public ManagerResponse update(ManagerRequest managerRequest, Long managerId) {
        Manager existedManager = this.getById(managerId);
        Manager updatedManager = this.managerMapper.toEntity(managerRequest);

        // Set fields from the existing manager that are not supposed to change
        updatedManager.setDateOfEmployment(existedManager.getDateOfEmployment());

        if (managerRequest.getManagerPermission() == ManagerPermission.FIRST_LEVEL)
            updatedManager.setUserRole(UserRole.TOP_MANAGER);
        else updatedManager.setUserRole(UserRole.MANAGER);


        if (updatedManager.getBaseSalary() == null || updatedManager.getBonus() == null) {
            updatedManager.setBaseSalary(existedManager.getBaseSalary());
            updatedManager.setBonus(existedManager.getBonus());
        }

        updatedManager.setId(managerId);
        // Check if the branch has a different manager before setting it
        Branch branch = branchService.getById(managerRequest.getBranchId());
        throwExceptionIfBranchHasAdifferentManager(branch, managerId);
        updatedManager.setBranch(branch);
        updatedManager.setTotalSalary(emp -> {
            double baseSalary = (emp.getBaseSalary() != null && !emp.getBaseSalary().trim().isEmpty())
                    ? Double.parseDouble(emp.getBaseSalary().trim()) : 0;
            double bonus = (emp.getBonus() != null && !emp.getBonus().trim().isEmpty())
                    ? Double.parseDouble(emp.getBonus().trim()) : 0;
            return baseSalary + bonus;
        });
        updatedManager = managerRepo.save(updatedManager);

        // Handle the ownership relationship
        Owner owner = ownerService.getById(managerRequest.getOwnerId());
        OwnersOfManagers ownersOfManagers = ownersOfManagersService.add(
                owner,
                updatedManager,
                OperationType.UPDATE
        );

        return this.managerMapper.toResponse(updatedManager);
    }
    @Override
    public UserResponse editProfile(UserRequest userRequest, Long userId) {
        Manager manager = getById(userId);
        Owner owner = ownerService.getByManagerId(manager.getId());

        ManagerRequest managerRequest = userMapper.toManagerRequest(userRequest);


        managerRequest.setManagerPermission(manager.getManagerPermission());
        managerRequest.setBranchId(manager.getBranch().getId());
        managerRequest.setOwnerId(owner.getId());


        ManagerResponse managerResponse = update(managerRequest,userId);

        return userMapper.toUserResponse(managerResponse);
    }
    @Override
    public void delete(Long managerId) {
        this.getById(managerId);
        managerRepo.deleteById(managerId);
    }
    @Override
    public List<ManagerResponse> getAll() {
        return managerRepo.findAll()
                .stream()
                .map(manager ->  managerMapper.toResponse(manager))
                .toList();
    }
    @Override
    public Optional<Manager> getObjectById(Long managerId) {
        return managerRepo.findById(managerId);
    }

    @Override
    public Manager getById(Long managerId) {
        return getObjectById(managerId).orElseThrow(
                () -> new NoSuchElementException("There is no manager with id = " + managerId)
        );
    }
    @Override
    public ManagerResponse getResponseById(Long managerId) {
        return managerMapper.toResponse(getById(managerId));
    }
    @Override
    public Manager getByBranchId(long branchId) {
        return managerRepo.findByBranchId(branchId).orElseThrow(
                () -> new NoSuchElementException("There is no manager with branch id = " + branchId)
        );
    }
    @Override
    public Manager getByOfferId(long offerId) {
        return managerRepo.findByPackageId(offerId).orElseThrow(
                () -> new NoSuchElementException("There is no manager for offer with id = " + offerId)
        );
    }
    @Override
    public ManagerResponse getResponseByBranchId(Long branchId) {
        return managerMapper.toResponse(getByBranchId(branchId));
    }






    @Override
    public WorkerResponse addWorker(WorkerRequest workerRequest){
        return workerService.add(workerRequest);
    }
    @Override
    public WorkerResponse updateWorker(WorkerRequest workerRequest, Long workerId){
        return workerService.update(workerRequest,workerId);
    }
    @Override
    public void deleteWorker(Long workerId){
        workerService.delete(workerId);
    }
    @Override
    public List<WorkerResponse> getAllWorkers(){
        return workerService.getAll();
    }
    @Override
    public WorkerResponse getWorkerResponseById(Long workerId){
        return workerService.getResponseById(workerId);
    }
    @Override
    public List<WorkerResponse> getAllWorkersByBranchId(Long branchId){
        return workerService.getAllByBranchId(branchId);
    }




    @Override
    public CustomerResponse updateCustomer(CustomerRequest customerRequest, Long customerId){
        return customerService.update(customerRequest,customerId);
    }

    @Override
    public void deleteCustomer(Long customerId){
        customerService.delete(customerId);
    }

    @Override
    public List<CustomerResponse> getAllCustomers(){
        return customerService.getAll();
    }

    @Override
    public CustomerResponse getCustomerById(Long customerId){
        return customerService.getResponseById(customerId);
    }




    @Override
    public ServiceResponse addService(ServiceRequest serviceRequest){
        return serviceService.add(serviceRequest);
    }
    @Override
    public ServiceResponse updateService(ServiceRequest serviceRequest, Long serviceId){
        return serviceService.update(serviceRequest,serviceId);
    }
    @Override
    public void deleteService(Long serviceId){
        serviceService.delete(serviceId);
    }
    @Override
    public List<ServiceResponse> getAllServices(){
        return serviceService.getAll();
    }

    @Override
    public List<ServiceResponse> getAllServicesByBranchId(Long branchId){
        return serviceService.getResponseAllByBranchId(branchId);
    }

    @Override
    public ServiceResponse getServiceResponseById(Long serviceId){
        return serviceService.getResponseById(serviceId);
    }
    @Override
    public ServicesOfBranchesResponse addServiceToBranch(ServicesOfBranchesRequest servicesOfBranchesRequest){
        return serviceService.addServiceToBranch(servicesOfBranchesRequest);
    }
    @Override
    public ServicesOfBranchesResponse activateServiceInBranch(ServicesOfBranchesRequest servicesOfBranchesRequest){
        return serviceService.activateServiceInBranch(servicesOfBranchesRequest);
    }
    @Override
    public ServicesOfBranchesResponse deactivateServiceInBranch(ServicesOfBranchesRequest servicesOfBranchesRequest){
        return serviceService.deactivateServiceInBranch(servicesOfBranchesRequest);
    }
    @Override
    public ServicesOfPackagesResponse addServiceToPackage(ServicesOfPackageRequest servicesOfPackageRequest){
        return serviceService.addServiceToPackage(servicesOfPackageRequest);
    }




    @Override
    public PackageResponse addPackage(PackageRequest packageRequest){
        return packageService.add(packageRequest);
    }
    @Override
    public PackageResponse updatePackage(PackageRequest packageRequest, Long offerId){
        return packageService.update(packageRequest,offerId);
    }
    @Override
    public void deletePackage(Long offerId){
        packageService.delete(offerId);
    }
    @Override
    public List<PackageResponse> getAllPackages(){
        return packageService.getAll();
    }
    @Override
    public PackageResponse getPackageResponseById(Long offerId){
        return packageService.getResponseById(offerId);
    }
    @Override
    public PackageOfBranchesResponse addPackageToBranch(PackageOfBranchesRequest packageOfBranchesRequest){
        return packageService.addPackageToBranch(packageOfBranchesRequest);
    }
    @Override
    public PackageOfBranchesResponse activatePackageInBranch(PackageOfBranchesRequest packageOfBranchesRequest){
        return packageService.activatePackageInBranch(packageOfBranchesRequest);
    }
    @Override
    public PackageOfBranchesResponse deactivatePackageInBranch(PackageOfBranchesRequest packageOfBranchesRequest){
        return packageService.deactivatePackageInBranch(packageOfBranchesRequest);
    }
    @Override
    public List<PackageResponse> getAllPackageResponseByBranchId(Long branchId){
        return packageService.getResponseAllByBranchId(branchId);
    }

}
