package com.GP.ELsayes.controller;

import com.GP.ELsayes.model.dto.ServiceRequest;
import com.GP.ELsayes.model.dto.relations.ServicesOfBranchesRequest;
import com.GP.ELsayes.model.dto.relations.ServicesOfOffersRequest;
import com.GP.ELsayes.service.ServiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/ELsayes-services")
public class ServiceController {
    @Autowired
    private ServiceService serviceService;

    @PostMapping("")
    public ResponseEntity<?> add(@RequestBody @Valid ServiceRequest serviceRequest) {
        return new ResponseEntity<>(this.serviceService.add(serviceRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{serviceId}")
    public ResponseEntity<?> update(@RequestBody @Valid ServiceRequest serviceRequest, @PathVariable Long serviceId){
        return new ResponseEntity<>(this.serviceService.update(serviceRequest , serviceId), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{serviceId}")
    public ResponseEntity<?> delete(@PathVariable Long serviceId){
        this.serviceService.delete(serviceId);
        return new ResponseEntity<>("Deleted successfully", HttpStatus.ACCEPTED);
    }

    @GetMapping("")
    ResponseEntity<?> getAll(){
        return new ResponseEntity<>(this.serviceService.getAll(), HttpStatus.OK);
    }


    @GetMapping("/get-by-id/{serviceId}")
    public ResponseEntity<?> getById(@PathVariable Long serviceId){
        return new ResponseEntity<>(this.serviceService.getResponseById(serviceId),HttpStatus.OK);
    }

    @PostMapping("/add-service-to-branch")
    public ResponseEntity<?> addServiceToBranch(@RequestBody @Valid ServicesOfBranchesRequest servicesOfBranchesRequest) {
        return new ResponseEntity<>(this.serviceService.addServiceToBranch(servicesOfBranchesRequest), HttpStatus.CREATED);
    }

    @PutMapping("/activate-service-in-branch")
    public ResponseEntity<?> activateServiceInBranch(@RequestBody @Valid ServicesOfBranchesRequest servicesOfBranchesRequest){
        return new ResponseEntity<>(this.serviceService.activateServiceInBranch(servicesOfBranchesRequest), HttpStatus.ACCEPTED);
    }

    @PutMapping("/deactivate-service-in-branch")
    public ResponseEntity<?> deactivateServiceInBranch(@RequestBody @Valid ServicesOfBranchesRequest servicesOfBranchesRequest){
        return new ResponseEntity<>(this.serviceService.deactivateServiceInBranch(servicesOfBranchesRequest), HttpStatus.ACCEPTED);
    }

    @GetMapping("/get-all-by-branch-id/{branchId}")
    public ResponseEntity<?> getAllByBranchId(@PathVariable Long branchId){
        return new ResponseEntity<>(this.serviceService.getResponseAllByBranchId(branchId),HttpStatus.OK);
    }

    @PostMapping("/add-service-to-offer")
    public ResponseEntity<?> addServiceToOffer(@RequestBody @Valid ServicesOfOffersRequest servicesOfOffersRequest) {
        return new ResponseEntity<>(this.serviceService.addServiceToOffer(servicesOfOffersRequest), HttpStatus.CREATED);
    }

    @GetMapping("/get-all-by-offer-id/{offerId}")
    public ResponseEntity<?> getAllByOfferId(@PathVariable Long offerId){
        return new ResponseEntity<>(this.serviceService.getResponseAllByOfferId(offerId),HttpStatus.OK);
    }


}
