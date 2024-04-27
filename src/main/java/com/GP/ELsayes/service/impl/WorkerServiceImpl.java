package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.CheckOutResponse;
import com.GP.ELsayes.model.dto.FreeTrialCodeResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.EditUserProfileRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.WorkerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.WorkerResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserResponse;
import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.Car;
import com.GP.ELsayes.model.entity.Order;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Worker;
import com.GP.ELsayes.model.entity.relations.VisitationsOfBranches;
import com.GP.ELsayes.model.enums.ProgressStatus;
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
    private final OrderService orderService;
    private final CarService carService;
    private final ServicesOfOrderService servicesOfOrderService;
    private final VisitationsOfBranchesService visitationsOfBranchesService;
    private final FreeTrialCodeService freeTrialCodeService;



    private void throwExceptionIfBranchNoteHasManager(Branch branch){
        if(branch.getManager() == null)
            throw new RuntimeException("This branch with id = "+ branch.getId() +" do not have a Manager yet");
        return;
    }

    private void throwExceptionIfCustomerHasAnOrderNotFinishedYet(Long customerId) {
        Optional<Order> unFinishedOrder = orderService.getUnFinishedOrderByCustomerId(customerId);
        if(unFinishedOrder.isEmpty()){
            return;
        }
        throw new RuntimeException("Customer whit id " + customerId +" has order not finished yet.");
    }

    private void throwExceptionIfUserNameAlreadyExist(String userName) {
        Optional<Worker> worker = workerRepo.findByUserName(userName);
        if(worker.isPresent())
            throw new RuntimeException("User name already exist");
    }


    @Override
    public WorkerResponse add(WorkerRequest workerRequest) {
        throwExceptionIfUserNameAlreadyExist(workerRequest.getUserName());

        Branch branch = this.branchService.getById(workerRequest.getBranchId());
        throwExceptionIfBranchNoteHasManager(branch);


        Worker worker = this.workerMapper.toEntity(workerRequest);
        worker.setWorkerStatus(WorkerStatus.UN_AVAILABLE);
        worker.setDateOfEmployment(new Date());
        worker.setScore("0");
        worker.setTotalSalary(emp -> {
            double baseSalary = Double.parseDouble(emp.getBaseSalary());
            double bonus = Double.parseDouble(emp.getBonus());
            return baseSalary + bonus;
        });
        if (workerRequest.getWorkerRole() == WorkerRole.CLEANING_WORKER)
            worker.setUserRole(UserRole.CLEANING_WORKER);
        else if (workerRequest.getWorkerRole() == WorkerRole.MAINTENANCE_WORKER) {
            worker.setUserRole(UserRole.MAINTENANCE_WORKER);
        }else worker.setUserRole(UserRole.PARKING_WORKER);


        worker.setBranch(branch);
        worker.setManager(branch.getManager());

        return this.workerMapper.toResponse(workerRepo.save(worker));
    }



    @SneakyThrows
    @Override
    public WorkerResponse update(WorkerRequest workerRequest, Long workerId) {
        throwExceptionIfUserNameAlreadyExist(workerRequest.getUserName());

        Worker existedWorker = this.getById(workerId);
        Worker updatedWorker = this.workerMapper.toEntity(workerRequest);

        // Set fields from the existing manager that are not supposed to change
        updatedWorker.setWorkerStatus(existedWorker.getWorkerStatus());
        updatedWorker.setDateOfEmployment(existedWorker.getDateOfEmployment());

        if (updatedWorker.getBaseSalary() == null || updatedWorker.getBonus() == null) {
            updatedWorker.setBaseSalary(existedWorker.getBaseSalary());
            updatedWorker.setBonus(existedWorker.getBonus());
        }

        if (workerRequest.getWorkerRole() == WorkerRole.CLEANING_WORKER)
            updatedWorker.setUserRole(UserRole.CLEANING_WORKER);
        else if (workerRequest.getWorkerRole() == WorkerRole.MAINTENANCE_WORKER) {
            updatedWorker.setUserRole(UserRole.MAINTENANCE_WORKER);
        }else updatedWorker.setUserRole(UserRole.PARKING_WORKER);


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

    @Override
    public void updateWorkerStatus(Long workerId,WorkerStatus workerStatus){
        Optional<Worker> worker = workerRepo.findById(workerId);
        WorkerRequest workerRequest = workerMapper.toRequest(worker.get());
        workerRequest.setBranchId(worker.get().getBranch().getId());
        workerRequest.setScore(worker.get().getScore());
        workerRequest.setWorkerStatus(workerStatus);
        update(workerRequest,workerId);
    }

    @Override
    public void changeWorkerStatus(Long workerId){
        Optional<Worker> worker = workerRepo.findById(workerId);
        if (worker.get().getWorkerStatus() == WorkerStatus.AVAILABLE)
            updateWorkerStatus(workerId, WorkerStatus.UN_AVAILABLE);
        else  updateWorkerStatus(workerId, WorkerStatus.AVAILABLE);
    }

    @Override
    public UserResponse editProfile(EditUserProfileRequest profileRequest, Long userId) {
        Worker worker = getById(userId);
        Branch branch = this.branchService.getByWorkerId(worker.getId());

        WorkerRequest workerRequest = userMapper.toWorkerRequest(profileRequest);
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

        visitationsOfBranchesService.addVisitation(car.get(),branch);
    }


    @Override
    public CheckOutResponse checkOut(String carPlateNumber, Long workerId) {

        Car car = carService.getByCarPlateNumber(carPlateNumber);
        Worker worker = getById(workerId);
        Optional<Branch> branch = branchService.getObjectById(worker.getBranch().getId());
        Optional<Customer> customer = Optional.ofNullable(car.getCustomer());


        VisitationsOfBranches customerVisitation = visitationsOfBranchesService.getRecentByCarPlateNumberAndBranchId(
                carPlateNumber,
                branch.get().getId()
        );
        if(customer.isPresent()){
            throwExceptionIfCustomerHasAnOrderNotFinishedYet(customer.get().getId());
        }
        visitationsOfBranchesService.endVisitation(car,branch.get());


        CheckOutResponse  checkOutResponse = new CheckOutResponse();
        checkOutResponse.setCarPlateNumber(carPlateNumber);
        checkOutResponse.setParkingPeriod(visitationsOfBranchesService.toFormattedPeriod(customerVisitation.getPeriod()));
        checkOutResponse.setParkingPrice(
                visitationsOfBranchesService.calculateParkingPrice(customerVisitation.getPeriod(),visitationsOfBranchesService.getHOUR_PRICE())
        );
        checkOutResponse.setTotalCost(checkOutResponse.getParkingPrice());

        if(customer.isPresent()){
            checkOutResponse.setCustomerName(customer.get().getFirstName() +" "+ customer.get().getLastName());
            Optional<Order> order = orderService.getFinishedByCustomerId(customer.get().getId());
            if (order.isPresent()){
                checkOutResponse.setOrder(orderService.getResponseFinishedOrderByCustomerId(customer.get().getId()));
                double currentTotalCost = Double.parseDouble(checkOutResponse.getTotalCost());
                double orderTotalCost = Double.parseDouble(checkOutResponse.getOrder().getOrderTotalCost());
                currentTotalCost += orderTotalCost;
                checkOutResponse.setTotalCost(String.valueOf(currentTotalCost));

                orderService.updateOrderStatus(order.get().getId(), ProgressStatus.PAYED);
            }
        }

        branchService.incrementProfit(branch.get().getId(),checkOutResponse.getTotalCost());
        return checkOutResponse;
    }

    public void finishTask(String carPlateNumber, Long workerId){
        Car car = carService.getByCarPlateNumber(carPlateNumber);
        Worker worker = getById(workerId);
        Optional<Branch> branch = branchService.getObjectById(worker.getBranch().getId());

        VisitationsOfBranches customerVisitation = visitationsOfBranchesService.getRecentByCarPlateNumberAndBranchId(
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

    @Override
    public FreeTrialCodeResponse generateFreeTrialCode(Long workerId){
        return freeTrialCodeService.generateCode(workerId);
    }

}
