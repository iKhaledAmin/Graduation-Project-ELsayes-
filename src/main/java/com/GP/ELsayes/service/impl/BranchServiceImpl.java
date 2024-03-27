package com.GP.ELsayes.service.impl;

import com.GP.ELsayes.model.dto.BranchRequest;
import com.GP.ELsayes.model.dto.BranchResponse;
import com.GP.ELsayes.model.dto.relations.VisitationsOfBranchesResponse;
import com.GP.ELsayes.model.entity.Branch;
import com.GP.ELsayes.model.entity.Offer;
import com.GP.ELsayes.model.entity.ServiceEntity;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.EmployeeChildren.Manager;
import com.GP.ELsayes.model.entity.relations.OwnersOfBranches;
import com.GP.ELsayes.model.entity.SystemUsers.userChildren.Owner;
import com.GP.ELsayes.model.enums.OperationType;
import com.GP.ELsayes.model.mapper.BranchMapper;
import com.GP.ELsayes.repository.BranchRepo;
import com.GP.ELsayes.service.*;
import com.GP.ELsayes.service.relations.VisitationsOfBranchesService;
import com.GP.ELsayes.service.relations.OwnersOfBranchesService;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class BranchServiceImpl implements BranchService {

    private final BranchMapper branchMapper;
    private final BranchRepo branchRepo;
    private final OwnerService ownerService;
    private final WorkerService workerService;

    private final ManagerService managerService;
    private final OwnersOfBranchesService ownersOfBranchesService;

    private final OfferService offerService;
    private final ServiceService serviceService;
    private final VisitationsOfBranchesService customerVisitationsService;


    public BranchServiceImpl(BranchMapper branchMapper, BranchRepo branchRepo, OwnerService ownerService,
                             @Lazy WorkerService workerService, @Lazy ManagerService managerService,
                             OwnersOfBranchesService ownersOfBranchesService, @Lazy OfferService offerService,
                             @Lazy ServiceService serviceService,@Lazy VisitationsOfBranchesService customerVisitationsService) {
        this.branchMapper = branchMapper;
        this.branchRepo = branchRepo;
        this.ownerService = ownerService;
        this.workerService = workerService;
        this.managerService = managerService;
        this.ownersOfBranchesService = ownersOfBranchesService;
        this.offerService = offerService;
        this.serviceService = serviceService;
        this.customerVisitationsService = customerVisitationsService;
    }
    // This method will run at midnight every day to reset the profitOfDay to zero
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetProfitOfDay() {
        List<Branch> branches = branchRepo.findAll();
        for (Branch branch : branches) {
            branch.setProfitOfDay("0");
            branchRepo.save(branch);
        }
    }

    // Runs on the first day of every month at midnight
    @Scheduled(cron = "0 0 0 1 * ?")
    public void resetProfitOfMonthTask() {
        List<Branch> branches = branchRepo.findAll();
        for (Branch branch : branches) {
            branch.setProfitOfMonth("0");
            branchRepo.save(branch);
        }
    }

    // Runs on the first day of every year at midnight
    @Scheduled(cron = "0 0 0 1 1 ?")
    public void resetProfitOfYearTask() {
        List<Branch> branches = branchRepo.findAll();
        for (Branch branch : branches) {
            branch.setProfitOfYear("0");
            branchRepo.save(branch);
        }
    }

    protected boolean hasManager(Long branchId){
        Optional<Manager> manager = managerService.getIfExistByBranchId(branchId);
        if (manager.isEmpty())return false;
        return true;
    }

    void throwExceptionIfBranchStillHasEmployees(Branch branch){
        int numberOfWorkers = workerService.getNumberOfWorkersByBranchId(branch.getId());
        if(numberOfWorkers != 0 || hasManager(branch.getId()))
            throw new RuntimeException("This branch with id = "+ branch.getId() +" still has employees, you can not delete it");
        return;
    }

    @Override
    public BranchResponse add(BranchRequest branchRequest) {
        Branch branch = this.branchMapper.toEntity(branchRequest);
        branch = this.branchRepo.save(branch);

        Owner owner = ownerService.getById(branchRequest.getOwnerId());
        OwnersOfBranches ownersOfBranches = ownersOfBranchesService.add(
                owner,
                branch,
                OperationType.CREATE
        );

        return this.branchMapper.toResponse(branch);
    }

    @SneakyThrows
    @Override
    public BranchResponse update(BranchRequest branchRequest, Long branchId) {

        Branch existedBranch = this.getById(branchId);
        Branch updatedBranch = this.branchMapper.toEntity(branchRequest);

        updatedBranch.setId(branchId);
        BeanUtils.copyProperties(existedBranch,updatedBranch);
        updatedBranch = branchRepo.save(updatedBranch);


        Owner owner = ownerService.getById(branchRequest.getOwnerId());
        OwnersOfBranches ownersOfBranches = ownersOfBranchesService.add(
                owner,
                updatedBranch,
                OperationType.UPDATE
        );

        return this.branchMapper.toResponse(updatedBranch);
    }

    @Override
    public void delete(Long branchId) {

        Branch branch =getById(branchId);
        throwExceptionIfBranchStillHasEmployees(branch);
        branchRepo.deleteById(branchId);
    }

    @Override
    public List<BranchResponse> getAll() {
        return branchRepo.findAll()
                .stream()
                .map(branch ->  branchMapper.toResponse(branch))
                .toList();
    }

    @Override
    public Branch getById(Long branchId) {
        return branchRepo.findById(branchId).orElseThrow(
                () -> new NoSuchElementException("There are no branch with id = " + branchId)
        );
    }

    @Override
    public BranchResponse getResponseById(Long branchId) {
        return branchMapper.toResponse(getById(branchId));
    }

    @Override
    public Branch getByManagerId(Long managerId) {
        return branchRepo.findByManagerId(managerId).orElseThrow(
                () -> new NoSuchElementException("There is no branch with manager id = " + managerId)
        );
    }

    @Override
    public BranchResponse getResponseByManagerId(Long managerId) {
        return branchMapper.toResponse(getByManagerId(managerId));
    }

    @Override
    public Branch getByWorkerId(Long workerId) {
        return branchRepo.findByWorkerId(workerId).orElseThrow(
                () -> new NoSuchElementException("There is no branch has worker with id = " + workerId)
        );
    }

    @Override
    public List<Branch> getAllByServiceId(Long serviceId) {
        ServiceEntity service = serviceService.getById(serviceId);
        return branchRepo.findAllByServiceId(serviceId);
    }

    @Override
    public List<Branch> getAllByOfferId(Long offerId) {
        Offer offer = offerService.getById(offerId);
        return branchRepo.findAllByOfferId(offerId);
    }

    @Override
    public List<VisitationsOfBranchesResponse> getResponseAllCurrentVisitationsInBranch(Long branchId) {
        return customerVisitationsService.getResponseAllCurrentVisitationsInBranch(branchId);
    }

    @Override
    public List<VisitationsOfBranchesResponse> getResponseAllVisitationsInBranchByADate(Long branchId, Date date) {
        return customerVisitationsService.getResponseAllVisitationsInBranchByADate(branchId,date);
    }


}
