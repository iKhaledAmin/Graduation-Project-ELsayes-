package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.CheckOutResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.WorkerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.WorkerResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserResponse;
import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.Car;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.GP.ELsayes.model.entity.relations.ServicesOfOrders;
import com.GP.ELsayes.model.entity.relations.VisitationsOfBranches;
import com.GP.ELsayes.model.enums.roles.UserRole;
import com.GP.ELsayes.model.enums.WorkerStatus;
import com.GP.ELsayes.model.enums.roles.WorkerRole;
import com.GP.ELsayes.model.mapper.UserMapper;
import com.GP.ELsayes.model.mapper.WorkerMapper;
import com.GP.ELsayes.repository.WorkerRepo;
import com.GP.ELsayes.service.*;
import com.GP.ELsayes.service.relations.ServicesOfOrderService;
import com.GP.ELsayes.service.relations.VisitationsOfBranchesService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WorkerServiceImpl implements UserService, WorkerService {

    private final  WorkerMapper workerMapper;
    private final  WorkerRepo workerRepo;
    private final UserMapper userMapper;
    private final BranchService branchService;
    private final CarService carService;
    private final ServicesOfOrderService servicesOfOrderService;
    private final VisitationsOfBranchesService customerVisitationService;



    void throwExceptionIfBranchNoteHasManager(Branch branch){
        if(branch.getManager() == null)
            throw new RuntimeException("This branch with id = "+ branch.getId() +" do not have a Manager yet");
        return;
    }




    @Override
    public WorkerResponse add(WorkerRequest workerRequest) {

        Branch branch = this.branchService.getById(workerRequest.getBranchId());
        throwExceptionIfBranchNoteHasManager(branch);


        Worker worker = this.workerMapper.toEntity(workerRequest);
        worker.setUserRole(UserRole.WORKER);
        worker.setWorkerStatus(WorkerStatus.AVAILABLE);
        worker.setDateOfEmployment(new Date());
        worker.setScore("0");
        worker.setTotalSalary(emp -> {
            double baseSalary = Double.parseDouble(emp.getBaseSalary());
            double bonus = Double.parseDouble(emp.getBonus());
            return baseSalary + bonus;
        });

        worker.setBranch(branch);
        worker.setManager(branch.getManager());

        return this.workerMapper.toResponse(workerRepo.save(worker));
    }



    @SneakyThrows
    @Override
    public WorkerResponse update(WorkerRequest workerRequest, Long workerId) {

        Worker existedWorker = this.getById(workerId);
        Worker updatedWorker = this.workerMapper.toEntity(workerRequest);

        // Set fields from the existing manager that are not supposed to change
        updatedWorker.setUserRole(existedWorker.getUserRole());
        updatedWorker.setWorkerStatus(existedWorker.getWorkerStatus());
        updatedWorker.setDateOfEmployment(existedWorker.getDateOfEmployment());

        if (updatedWorker.getBaseSalary() == null || updatedWorker.getBonus() == null) {
            updatedWorker.setBaseSalary(existedWorker.getBaseSalary());
            updatedWorker.setBonus(existedWorker.getBonus());
        }


        updatedWorker.setId(workerId);
        BeanUtils.copyProperties(existedWorker,updatedWorker);
        updatedWorker.setWorkerStatus(workerRequest.getWorkerStatus());
        updatedWorker.setScore(workerRequest.getScore());
        updatedWorker.setTotalSalary(emp -> {
            double baseSalary = Double.parseDouble(emp.getBaseSalary());
            double bonus = Double.parseDouble(emp.getBonus());
            return baseSalary + bonus;
        });

        Branch branch = this.branchService.getById(workerRequest.getBranchId());
        updatedWorker.setBranch(branch);
        updatedWorker.setManager(branch.getManager());



        return this.workerMapper.toResponse(workerRepo.save(updatedWorker));
    }

    public void updateWorkerStatus(Long workerId,WorkerStatus workerStatus){
        Optional<Worker> worker = workerRepo.findById(workerId);
        WorkerRequest workerRequest = workerMapper.toRequest(worker.get());
        workerRequest.setBranchId(worker.get().getBranch().getId());
        workerRequest.setScore(worker.get().getScore());
        workerRequest.setWorkerStatus(workerStatus);
        update(workerRequest,workerId);
    }

    @Override
    public UserResponse editProfile(UserRequest userRequest, Long userId) {
        Worker worker = getById(userId);
        Branch branch = this.branchService.getByWorkerId(worker.getId());

        WorkerRequest workerRequest = userMapper.toWorkerRequest(userRequest);
        workerRequest.setBranchId(branch.getId());

        WorkerResponse workerResponse = update(workerRequest,userId);

        return userMapper.toUserResponse(workerResponse);
    }

    @Override
    public void delete(Long workerId) {
        getById(workerId);
        workerRepo.deleteById(workerId);
    }

    @Override
    public List<WorkerResponse> getAll() {
        return workerRepo.findAll()
                .stream()
                .map(worker ->  workerMapper.toResponse(worker))
                .toList();
    }

    @Override
    public Optional<Worker> getObjectById(Long workerId) {
        return workerRepo.findById(workerId);
    }

    @Override
    public Worker getById(Long workerId) {
        return getObjectById(workerId).orElseThrow(
                () -> new NoSuchElementException("There is no worker with id = " + workerId)
        );
    }


    @Override
    public WorkerResponse getResponseById(Long workerId) {
        return workerMapper.toResponse(getById(workerId));
    }


    @Override
    public List<WorkerResponse> getAllByBranchId(Long branchId) {
        return workerRepo.findAllWorkersByBranchId(branchId).get()
                .stream().map(worker ->  workerMapper.toResponse(worker))
                .collect(Collectors.toList());
    }

    @Override
    public void recordVisitation(String carPlateNumber , Long branchId) {
        Branch branch = branchService.getById(branchId);

        Optional<Car> car = carService.getIfExistByCarPlateNumber(carPlateNumber);

        if (car.isEmpty()){
            Car newCar = new Car();
            newCar.setCarPlateNumber(carPlateNumber);
            car = Optional.ofNullable(carService.add(newCar));
        }

        customerVisitationService.addVisitation(car.get(),branch);
    }


    @Override
    public CheckOutResponse checkOut(String carPlateNumber, Long workerId) {

        Car car = carService.getByCarPlateNumber(carPlateNumber);
        Optional<Worker> worker = getObjectById(workerId);
        Optional<Branch> branch = branchService.getObjectById(worker.get().getBranch().getId());
        Optional<Customer> customer = Optional.ofNullable(car.getCustomer());

        VisitationsOfBranches customerVisitation = customerVisitationService.getRecentByCarPlateNumberAndBranchId(
                carPlateNumber,
                branch.get().getId()
        );

        customerVisitationService.endVisitation(car,branch.get());

        CheckOutResponse  checkOutResponse = new CheckOutResponse();
        checkOutResponse.setCarPlateNumber(carPlateNumber);
        checkOutResponse.setPeriodParking(customerVisitation.getPeriod());
        if(customer.isPresent()){
            checkOutResponse.setCustomerName(customer.get().getFirstName() +" "+ customer.get().getLastName());
        }

        return checkOutResponse;
    }

    public void finishTask(String carPlateNumber, Long workerId){
        Car car = carService.getByCarPlateNumber(carPlateNumber);
        Optional<Worker> worker = getObjectById(workerId);
        Optional<Branch> branch = branchService.getObjectById(worker.get().getBranch().getId());

        VisitationsOfBranches customerVisitation = customerVisitationService.getRecentByCarPlateNumberAndBranchId(
                carPlateNumber,
                branch.get().getId()
        );

        Optional<Customer> customer = Optional.ofNullable(car.getCustomer());
        if (customer.isEmpty())
            throw new RuntimeException("This car with plate number = "+ carPlateNumber +" do not follow any of our customer");

        servicesOfOrderService.finishServiceTask(customer.get().getId(),workerId);
    }

    @Override
    public Integer getNumberOfWorkersByBranchId(Long branchId) {
        return workerRepo.getNumberOfWorkersByBranchId(branchId);
    }

    @Override
    public List<Worker> getAllAvailableWorkerByWorkerRoleOrderByScore(WorkerRole workerRole) {
        return workerRepo.findAllAvailableWorkerByWorkerRoleOrderByScore(workerRole);
    }




}
