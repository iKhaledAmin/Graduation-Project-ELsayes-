package com.GP.ELsayes.service.relations.impl;


import com.GP.ELsayes.model.dto.relations.VisitationsOfBranchesResponse;
import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.Car;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;

import com.GP.ELsayes.model.entity.relations.VisitationsOfBranches;
import com.GP.ELsayes.model.mapper.relations.VisitationsOfBranchesMapper;
import com.GP.ELsayes.repository.relations.VisitationsOfBranchesRepo;
import com.GP.ELsayes.service.CarService;

import com.GP.ELsayes.service.relations.VisitationsOfBranchesService;
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
public class VisitationsOfBranchesServiceImpl implements VisitationsOfBranchesService {

    private final CarService carService;
    private final VisitationsOfBranchesMapper visitationsOfBranchesMapper;
    private final VisitationsOfBranchesRepo visitationsOfBranchesRepo;


    private void throwExceptionIfCarHasAlreadyRecorded(String carPlateNumber, Long branchId){
        Optional<VisitationsOfBranches> customerVisitation
                = visitationsOfBranchesRepo.findCurrentlyByCarPlateNumberAndBranchId(carPlateNumber,branchId);
        if(customerVisitation.isEmpty()){
            return;
        }
        throw new RuntimeException("This car with plate number = " + carPlateNumber +" already recorded");
    }

    private void throwExceptionIfCarHasAlreadyCheckedOut(String carPlateNumber, Long branchId) {
        Optional<VisitationsOfBranches> customerVisitation
                = visitationsOfBranchesRepo.findRecentByCarPlateNumberAndBranchId(carPlateNumber, branchId, PageRequest.of(0, 1))
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
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;

        // Format the period string
        return String.format("%02dd:%02dh:%02dm", days, hours, minutes);
    }
    @SneakyThrows
    private VisitationsOfBranches update(VisitationsOfBranches visitation){

        VisitationsOfBranches updatedCustomerVisitation = visitation;
        VisitationsOfBranches existedCustomerVisitation = getRecentByCarPlateNumberAndBranchId(
                visitation.getCar().getCarPlateNumber(),
                visitation.getBranch().getId()
        );


        updatedCustomerVisitation.setId(existedCustomerVisitation.getId());
        BeanUtils.copyProperties(existedCustomerVisitation,updatedCustomerVisitation);

        return visitationsOfBranchesRepo.save(updatedCustomerVisitation);
    }



    @Override
    public VisitationsOfBranches getRecentByCarPlateNumberAndBranchId(String carPlateNumber, Long branchId) {
        return visitationsOfBranchesRepo.findRecentByCarPlateNumberAndBranchId(carPlateNumber, branchId, PageRequest.of(0, 1))
                .getContent()
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("There is no car with plate number = " + carPlateNumber + " currently in this branch."));
    }

    @Override
    public List<VisitationsOfBranchesResponse> getResponseAllCurrentVisitationsInBranch(Long branchId) {
        return visitationsOfBranchesRepo.findAllCurrentVisitationsInBranch(branchId)
                .stream()
                .map(CurrentVisitation ->  visitationsOfBranchesMapper.toResponse(CurrentVisitation))
                .toList();
    }

    @Override
    public List<VisitationsOfBranchesResponse> getResponseAllVisitationsInBranchByADate(Long branchId, Date date) {
        if (date == null) {
            date = java.sql.Date.valueOf(LocalDate.now());
        }
        return visitationsOfBranchesRepo.findAVisitationsInBranchByADate(branchId, date)
                .stream()
                .map(CurrentVisitation -> visitationsOfBranchesMapper.toResponse(CurrentVisitation))
                .toList();
    }

    @Override
    public Optional<VisitationsOfBranches> getCurrentVisitationByCustomerId(Long customerId) {
        return visitationsOfBranchesRepo.findCurrentVisitationByCustomerId(customerId);
    }


    @Override
    public VisitationsOfBranches addVisitation(Car car, Branch branch) {
        throwExceptionIfCarHasAlreadyRecorded(car.getCarPlateNumber(), branch.getId());


        VisitationsOfBranches customerVisitation = new com.GP.ELsayes.model.entity.relations.VisitationsOfBranches();
        customerVisitation.setBranch(branch);
        customerVisitation.setCar(car);
        customerVisitation.setDateOfArriving(new Date());

        Optional<Customer> customer = Optional.ofNullable(car.getCustomer());
        if(customer.isPresent()){
            customerVisitation.setCustomer(customer.get());
        }

        return visitationsOfBranchesRepo.save(customerVisitation);
    }




    @Override
    public VisitationsOfBranches endVisitation(Car car, Branch branch) {
        com.GP.ELsayes.model.entity.relations.VisitationsOfBranches existedCustomerVisitation = getRecentByCarPlateNumberAndBranchId(
                car.getCarPlateNumber(),
                branch.getId()
        );

        throwExceptionIfCarHasAlreadyCheckedOut(
                car.getCarPlateNumber(),
                branch.getId()
        );

        existedCustomerVisitation.setDateOfLeaving(new Date());
        existedCustomerVisitation.setPeriod(calculatePeriod(
                existedCustomerVisitation.getDateOfArriving(),
                existedCustomerVisitation.getDateOfLeaving())
        );
        Optional<Customer> customer = Optional.ofNullable(car.getCustomer());
        if(customer.isPresent()){
            existedCustomerVisitation.setCustomer(customer.get());
        }

        return update(existedCustomerVisitation);
    }

}
