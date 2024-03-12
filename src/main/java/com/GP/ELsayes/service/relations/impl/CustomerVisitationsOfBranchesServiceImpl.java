package com.GP.ELsayes.service.relations.impl;

import com.GP.ELsayes.model.dto.relations.CustomerVisitationsOfBranchesRequest;
import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.Car;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Customer;
import com.GP.ELsayes.model.entity.relations.CustomerVisitationsOfBranches;
import com.GP.ELsayes.service.BranchService;
import com.GP.ELsayes.service.CarService;
import com.GP.ELsayes.service.CustomerService;
import com.GP.ELsayes.service.relations.CustomerVisitationsOfBranchesService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class CustomerVisitationsOfBranchesServiceImpl implements CustomerVisitationsOfBranchesService {

    private final CustomerService customerService;
    private final BranchService branchService;
    private final CarService carService;

    @Override
    public CustomerVisitationsOfBranches add(CustomerVisitationsOfBranchesRequest customerVisitationRequest) {

        Customer customer = customerService.getById(customerVisitationRequest.getCustomerId());
        Branch branch = branchService.getById(customerVisitationRequest.getBranchId());
        Car car = carService.getById(customer.getCar().getId());

        CustomerVisitationsOfBranches customerVisitation = new CustomerVisitationsOfBranches();
        customerVisitation.setCustomer(customer);
        customerVisitation.setBranch(branch);
        customerVisitation.setCar(car);
        customerVisitation.setDateOfArriving(new Date());

        return null;
    }
}
