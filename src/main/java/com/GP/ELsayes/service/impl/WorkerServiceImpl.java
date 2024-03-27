package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.CheckOutResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.WorkerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.WorkerResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserResponse;
import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.Car;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.GP.ELsayes.model.entity.relations.VisitationsOfBranches;
import com.GP.ELsayes.model.entity.relations.WorkersOfBranches;
import com.GP.ELsayes.model.enums.roles.UserRole;
import com.GP.ELsayes.model.enums.WorkerStatus;
import com.GP.ELsayes.model.mapper.UserMapper;
import com.GP.ELsayes.model.mapper.WorkerMapper;
import com.GP.ELsayes.repository.WorkerRepo;
import com.GP.ELsayes.service.*;
import com.GP.ELsayes.service.relations.VisitationsOfBranchesService;
import com.GP.ELsayes.service.relations.WorkersOfBranchesService;
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
    private final CustomerService customerService;
    private final CarService carService;
    private final WorkersOfBranchesService workersOfBranchesService;
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
        worker.setTotalSalary(emp -> {
            double baseSalary = Double.parseDouble(emp.getBaseSalary());
            double bonus = Double.parseDouble(emp.getBonus());
            return baseSalary + bonus;
        });

        worker = workerRepo.save(worker);

        WorkersOfBranches workersOfBranches = workersOfBranchesService.addWorkerToBranch(worker, branch);
        worker.setWorkersOfBranch(List.of(workersOfBranches));


        return this.workerMapper.toResponse(worker);
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
        updatedWorker.setTotalSalary(emp -> {
            double baseSalary = Double.parseDouble(emp.getBaseSalary());
            double bonus = Double.parseDouble(emp.getBonus());
            return baseSalary + bonus;
        });
        updatedWorker = workerRepo.save(updatedWorker);

        Branch branch = this.branchService.getById(workerRequest.getBranchId());
        WorkersOfBranches workersOfBranches = workersOfBranchesService.updateWorkerOfBranch(
                                            updatedWorker.getId(),
                                            branch.getId()
                                            );
        updatedWorker.setWorkersOfBranch(List.of(workersOfBranches));



        return this.workerMapper.toResponse(updatedWorker);
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
    public Worker getById(Long workerId) {
        return workerRepo.findById(workerId).orElseThrow(
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
    public CheckOutResponse checkOut(String carPlateNumber, Long branchId) {

        Car car = carService.getByCarPlateNumber(carPlateNumber);
        Branch branch = branchService.getById(branchId);
        Optional<Customer> customer = Optional.ofNullable(car.getCustomer());

        VisitationsOfBranches customerVisitation = customerVisitationService.getRecentByCarPlateNumberAndBranchId(
                carPlateNumber,
                branch.getId()
        );

        customerVisitationService.endVisitation(car,branch);

        CheckOutResponse  checkOutResponse = new CheckOutResponse();
        checkOutResponse.setCarPlateNumber(carPlateNumber);
        checkOutResponse.setPeriodParking(customerVisitation.getPeriod());
        if(customer.isPresent()){
            checkOutResponse.setCustomerName(customer.get().getFirstName() +" "+ customer.get().getLastName());
        }

        return checkOutResponse;
    }

    @Override
    public Integer getNumberOfWorkersByBranchId(Long branchId) {
        return workerRepo.getNumberOfWorkersByBranchId(branchId);
    }



}
