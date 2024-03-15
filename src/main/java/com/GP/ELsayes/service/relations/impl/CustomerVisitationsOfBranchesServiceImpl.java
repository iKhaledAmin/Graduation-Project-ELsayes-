package com.GP.ELsayes.service.relations.impl;

import com.GP.ELsayes.model.dto.CarResponse;
import com.GP.ELsayes.model.dto.CustomerVisitationsResponse;
import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.Car;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.entity.relations.CustomerVisitationsOfBranches;
import com.GP.ELsayes.model.mapper.relations.CustomerVisitationsMapper;
import com.GP.ELsayes.repository.relations.CustomerVisitationsOfBranchesRepo;
import com.GP.ELsayes.service.CarService;
import com.GP.ELsayes.service.relations.CustomerVisitationsOfBranchesService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerVisitationsOfBranchesServiceImpl implements CustomerVisitationsOfBranchesService {

    private final CarService carService;
    private final CustomerVisitationsMapper customerVisitationsMapper;
    private final CustomerVisitationsOfBranchesRepo customerVisitationsOfBranchesRepo;


    private void throwExceptionIfCarHasAlreadyRecorded(String carPlateNumber, Long branchId){
        Optional<CustomerVisitationsOfBranches> customerVisitation
                = customerVisitationsOfBranchesRepo.findCurrentlyByCarPlateNumberAndBranchId(carPlateNumber,branchId);
        if(customerVisitation.isEmpty()){
            return;
        }
        throw new RuntimeException("This car with plate number = " + carPlateNumber +" already recorded");
    }

    private void throwExceptionIfCarHasAlreadyCheckedOut(String carPlateNumber, Long branchId) {
        Optional<CustomerVisitationsOfBranches> customerVisitation
                = customerVisitationsOfBranchesRepo.findRecentByCarPlateNumberAndBranchId(carPlateNumber, branchId, PageRequest.of(0, 1))
                .getContent()
                .stream()
                .findFirst();

        if (customerVisitation.isPresent() && customerVisitation.get().getDateOfLeaving() != null) {
            throw new RuntimeException("This car with plate number = " + carPlateNumber + " has already checked out before");
        }
    }


    private String calculatePeriod(Date dateOfArriving, Date dateOfLeaving) {
        Instant start = dateOfArriving.toInstant();
        Instant end = dateOfLeaving.toInstant();

        Duration duration = Duration.between(start, end);

        // Get the duration components
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        // Format the period string
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }


    @Override
    public CustomerVisitationsOfBranches getRecentByCarPlateNumberAndBranchId(String carPlateNumber, Long branchId) {
        return customerVisitationsOfBranchesRepo.findRecentByCarPlateNumberAndBranchId(carPlateNumber, branchId, PageRequest.of(0, 1))
                .getContent()
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("There is no car with plate number = " + carPlateNumber + " currently in this branch."));
    }

    @Override
    public List<CustomerVisitationsResponse> getResponseAllCurrentVisitationsInBranch(Long branchId) {
        return customerVisitationsOfBranchesRepo.findAllCurrentVisitationsInBranch(branchId)
                .stream()
                .map(CurrentVisitation ->  customerVisitationsMapper.toResponse(CurrentVisitation))
                .toList();
    }

    @Override
    public List<CustomerVisitationsResponse> getResponseAllVisitationsInBranchByADate(Long branchId, Date date) {
        if (date == null) {
            date = java.sql.Date.valueOf(LocalDate.now());
        }
        return customerVisitationsOfBranchesRepo.findAVisitationsInBranchByADate(branchId, date)
                .stream()
                .map(CurrentVisitation -> customerVisitationsMapper.toResponse(CurrentVisitation))
                .toList();
    }


    @Override
    public CustomerVisitationsOfBranches add(Customer customer , Branch branch) {
        Car car = carService.getById(customer.getCar().getId());
        throwExceptionIfCarHasAlreadyRecorded(car.getCarPlateNumber(), branch.getId());


        CustomerVisitationsOfBranches customerVisitation = new CustomerVisitationsOfBranches();
        customerVisitation.setCustomer(customer);
        customerVisitation.setBranch(branch);
        customerVisitation.setCar(car);
        customerVisitation.setDateOfArriving(new Date());

        return customerVisitationsOfBranchesRepo.save(customerVisitation);
    }

    @SneakyThrows
    private CustomerVisitationsOfBranches update(CustomerVisitationsOfBranches customerVisitation){

        CustomerVisitationsOfBranches updatedCustomerVisitation = customerVisitation;
        CustomerVisitationsOfBranches existedCustomerVisitation = getRecentByCarPlateNumberAndBranchId(
                customerVisitation.getCar().getCarPlateNumber(),
                customerVisitation.getBranch().getId()
        );


        updatedCustomerVisitation.setId(existedCustomerVisitation.getId());
        BeanUtils.copyProperties(existedCustomerVisitation,updatedCustomerVisitation);

        return customerVisitationsOfBranchesRepo.save(updatedCustomerVisitation);
    }

    @Override
    public CustomerVisitationsOfBranches endVisitation(CustomerVisitationsOfBranches customerVisitation) {
        CustomerVisitationsOfBranches existedCustomerVisitation = getRecentByCarPlateNumberAndBranchId(
                customerVisitation.getCar().getCarPlateNumber(),
                customerVisitation.getBranch().getId()
        );

        throwExceptionIfCarHasAlreadyCheckedOut(
                customerVisitation.getCar().getCarPlateNumber(),
                customerVisitation.getBranch().getId()
        );

        existedCustomerVisitation.setDateOfLeaving(new Date());
        existedCustomerVisitation.setPeriod(calculatePeriod(
                existedCustomerVisitation.getDateOfArriving(),
                existedCustomerVisitation.getDateOfLeaving())
        );
        return update(existedCustomerVisitation);
    }

}
