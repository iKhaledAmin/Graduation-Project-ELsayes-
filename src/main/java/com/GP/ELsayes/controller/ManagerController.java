package com.GP.ELsayes.controller;

import com.GP.ELsayes.model.dto.OfferRequest;
import com.GP.ELsayes.model.dto.ServiceRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.ManagerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.WorkerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserRequest;
import com.GP.ELsayes.model.dto.relations.OffersOfBranchesRequest;
import com.GP.ELsayes.model.dto.relations.ServicesOfBranchesRequest;
import com.GP.ELsayes.model.dto.relations.ServicesOfOffersRequest;
import com.GP.ELsayes.service.ManagerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/managers")
public class ManagerController {
    @Autowired
private ManagerService managerService;


    @PutMapping("/edit-profile/{managerId}")
    public ResponseEntity<?> editProfile(@RequestBody @Valid UserRequest userRequest, @PathVariable Long managerId){
        return new ResponseEntity<>(this.managerService.editProfile(userRequest , managerId), HttpStatus.ACCEPTED);
    }



    @PostMapping("/add-worker")
    public ResponseEntity<?> addWorker(@RequestBody @Valid WorkerRequest workerRequest) {
        return new ResponseEntity<>(this.managerService.addWorker(workerRequest), HttpStatus.CREATED);
    }
    @PutMapping("/update-worker/{workerId}")
    public ResponseEntity<?> updateWorker(@RequestBody @Valid WorkerRequest workerRequest, @PathVariable Long workerId){
        return new ResponseEntity<>(this.managerService.updateWorker(workerRequest , workerId), HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/delete-worker/{workerId}")
    public ResponseEntity<?> deleteWorker(@PathVariable Long workerId){
        this.managerService.deleteWorker(workerId);
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.ACCEPTED);
    }
    @GetMapping("/get-all-workers")
    ResponseEntity<?> getAllWorker(){
        return new ResponseEntity<>(this.managerService.getAllWorkers(), HttpStatus.OK);
    }
    @GetMapping("/get-worker-by-id/{workerId}")
    public ResponseEntity<?> getWorkerById(@PathVariable Long workerId){
        return new ResponseEntity<>(this.managerService.getWorkerResponseById(workerId),HttpStatus.OK);
    }
    @GetMapping("/get-all-workers-by-branchId/{branchId}")
    public ResponseEntity<?> getAllWorkersByBranchId(@PathVariable Long branchId){
        return new ResponseEntity<>(this.managerService.getAllWorkersByBranchId(branchId), HttpStatus.OK);
    }




    @PostMapping("/add-service")
    public ResponseEntity<?> addService(@RequestBody @Valid ServiceRequest serviceRequest) {
        return new ResponseEntity<>(this.managerService.addService(serviceRequest), HttpStatus.CREATED);
    }
    @PutMapping("/update-service/{serviceId}")
    public ResponseEntity<?> updateService(@RequestBody @Valid ServiceRequest serviceRequest, @PathVariable Long serviceId){
        return new ResponseEntity<>(this.managerService.updateService(serviceRequest , serviceId), HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/delete-service/{serviceId}")
    public ResponseEntity<?> deleteService(@PathVariable Long serviceId){
        this.managerService.deleteService(serviceId);
        return new ResponseEntity<>("Deleted successfully", HttpStatus.ACCEPTED);
    }
    @GetMapping("/get-all-services")
    ResponseEntity<?> getAllService(){
        return new ResponseEntity<>(this.managerService.getAllServices(), HttpStatus.OK);
    }
    @GetMapping("/get-service-by-id/{serviceId}")
    public ResponseEntity<?> getServiceById(@PathVariable Long serviceId){
        return new ResponseEntity<>(this.managerService.getServiceResponseById(serviceId),HttpStatus.OK);
    }
    @PostMapping("/add-service-to-branch")
    public ResponseEntity<?> addServiceToBranch(@RequestBody @Valid ServicesOfBranchesRequest servicesOfBranchesRequest) {
        return new ResponseEntity<>(this.managerService.addServiceToBranch(servicesOfBranchesRequest), HttpStatus.CREATED);
    }
    @PutMapping("/activate-service-in-branch")
    public ResponseEntity<?> activateServiceInBranch(@RequestBody @Valid ServicesOfBranchesRequest servicesOfBranchesRequest){
        return new ResponseEntity<>(this.managerService.activateServiceInBranch(servicesOfBranchesRequest), HttpStatus.ACCEPTED);
    }
    @PutMapping("/deactivate-service-in-branch")
    public ResponseEntity<?> deactivateServiceInBranch(@RequestBody @Valid ServicesOfBranchesRequest servicesOfBranchesRequest){
        return new ResponseEntity<>(this.managerService.deactivateServiceInBranch(servicesOfBranchesRequest), HttpStatus.ACCEPTED);
    }

    @PostMapping("/add-service-to-offer")
    public ResponseEntity<?> addServiceToOffer(@RequestBody @Valid ServicesOfOffersRequest servicesOfOffersRequest) {
        return new ResponseEntity<>(this.managerService.addServiceToOffer(servicesOfOffersRequest), HttpStatus.CREATED);
    }

    @GetMapping("/get-all-service-by-branch-id/{branchId}")
    public ResponseEntity<?> getServiceAllByBranchId(@PathVariable Long branchId){
        return new ResponseEntity<>(this.managerService.getAllServicesByBranchId(branchId),HttpStatus.OK);
    }





    @PostMapping("/add-offer")
    public ResponseEntity<?> addOffer(@RequestBody @Valid OfferRequest offerRequest) {
        return new ResponseEntity<>(this.managerService.addOffer(offerRequest), HttpStatus.CREATED);
    }
    @PutMapping("/update-offer/{offerId}")
    public ResponseEntity<?> updateOffer(@RequestBody @Valid OfferRequest offerRequest,@PathVariable Long offerId){
        return new ResponseEntity<>(this.managerService.updateOffer(offerRequest , offerId), HttpStatus.ACCEPTED);
    }
    @GetMapping("/get-offer-by-id/{offerId}")
    public ResponseEntity<?> getOfferById(@PathVariable Long offerId){
        return new ResponseEntity<>(this.managerService.getOfferResponseById(offerId),HttpStatus.OK);
    }
    @DeleteMapping("/delete-offer/{offerId}")
    public ResponseEntity<?> deleteOffer(@PathVariable Long offerId){
        this.managerService.deleteOffer(offerId);
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.ACCEPTED);
    }
    @GetMapping("get-all-offers")
    ResponseEntity<?> getAllOffers(){
        return new ResponseEntity<>(this.managerService.getAllOffers(), HttpStatus.OK);
    }
    @PostMapping("/add-offer-to-branch")
    public ResponseEntity<?> addOfferToBranch(@RequestBody @Valid OffersOfBranchesRequest offersOfBranchesRequest) {
        return new ResponseEntity<>(this.managerService.addOfferToBranch(offersOfBranchesRequest), HttpStatus.CREATED);
    }
    @PutMapping("/activate-offer-in-branch")
    public ResponseEntity<?> activateServiceInBranch(@RequestBody @Valid OffersOfBranchesRequest offersOfBranchesRequest){
        return new ResponseEntity<>(this.managerService.activateOfferInBranch(offersOfBranchesRequest), HttpStatus.ACCEPTED);
    }
    @PutMapping("/deactivate-offer-in-branch")
    public ResponseEntity<?> deactivateServiceInBranch(@RequestBody @Valid OffersOfBranchesRequest offersOfBranchesRequest){
        return new ResponseEntity<>(this.managerService.deactivateOfferInBranch(offersOfBranchesRequest), HttpStatus.ACCEPTED);
    }
    @GetMapping("/get-all-offer-by-branch-id/{branchId}")
    public ResponseEntity<?> getAllOffersByBranchId(@PathVariable Long branchId){
        return new ResponseEntity<>(this.managerService.getAllOfferResponseByBranchId(branchId),HttpStatus.OK);
    }



}
