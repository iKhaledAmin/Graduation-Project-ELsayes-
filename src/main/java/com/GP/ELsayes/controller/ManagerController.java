package com.GP.ELsayes.controller;

import com.GP.ELsayes.model.dto.PackageRequest;
import com.GP.ELsayes.model.dto.ServiceRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.WorkerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserRequest;
import com.GP.ELsayes.model.dto.relations.PackageOfBranchesRequest;
import com.GP.ELsayes.model.dto.relations.ServicesOfBranchesRequest;
import com.GP.ELsayes.model.dto.relations.ServicesOfPackageRequest;
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




    @PutMapping("/update-customer/{customerId}")
    public ResponseEntity<?> updateCustomer(@RequestBody @Valid CustomerRequest customerRequest, @PathVariable Long customerId){
        return new ResponseEntity<>(this.managerService.updateCustomer(customerRequest , customerId), HttpStatus.ACCEPTED);
    }


    @DeleteMapping("/delete-customer/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long customerId){
        this.managerService.deleteCustomer(customerId);
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.ACCEPTED);
    }

    @GetMapping("/get-all-customers")
    ResponseEntity<?> getAllCustomers(){
        return new ResponseEntity<>(this.managerService.getAllCustomers(), HttpStatus.OK);
    }
    @GetMapping("/get-customer-by-id/{customerId}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long customerId){
        return new ResponseEntity<>(this.managerService.getCustomerById(customerId),HttpStatus.OK);
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

    @PostMapping("/add-service-to-package")
    public ResponseEntity<?> addServiceToPackage(@RequestBody @Valid ServicesOfPackageRequest servicesOfPackageRequest) {
        return new ResponseEntity<>(this.managerService.addServiceToPackage(servicesOfPackageRequest), HttpStatus.CREATED);
    }

    @GetMapping("/get-all-service-by-branch-id/{branchId}")
    public ResponseEntity<?> getServiceAllByBranchId(@PathVariable Long branchId){
        return new ResponseEntity<>(this.managerService.getAllServicesByBranchId(branchId),HttpStatus.OK);
    }





    @PostMapping("/add-package")
    public ResponseEntity<?> addPackage(@RequestBody @Valid PackageRequest packageRequest) {
        return new ResponseEntity<>(this.managerService.addPackage(packageRequest), HttpStatus.CREATED);
    }
    @PutMapping("/update-package/{packageId}")
    public ResponseEntity<?> updatePackage(@RequestBody @Valid PackageRequest packageRequest, @PathVariable Long packageId){
        return new ResponseEntity<>(this.managerService.updatePackage(packageRequest, packageId), HttpStatus.ACCEPTED);
    }
    @GetMapping("/get-package-by-id/{packageId}")
    public ResponseEntity<?> getPackageById(@PathVariable Long packageId){
        return new ResponseEntity<>(this.managerService.getPackageResponseById(packageId),HttpStatus.OK);
    }
    @DeleteMapping("/delete-package/{packageId}")
    public ResponseEntity<?> deletePackage(@PathVariable Long packageId){
        this.managerService.deletePackage(packageId);
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.ACCEPTED);
    }
    @GetMapping("get-all-packages")
    ResponseEntity<?> getAllOffers(){
        return new ResponseEntity<>(this.managerService.getAllPackages(), HttpStatus.OK);
    }
    @PostMapping("/add-package-to-branch")
    public ResponseEntity<?> addPackageToBranch(@RequestBody @Valid PackageOfBranchesRequest packageOfBranchesRequest) {
        return new ResponseEntity<>(this.managerService.addPackageToBranch(packageOfBranchesRequest), HttpStatus.CREATED);
    }
    @PutMapping("/activate-package-in-branch")
    public ResponseEntity<?> activatePackageInBranch(@RequestBody @Valid PackageOfBranchesRequest packageOfBranchesRequest){
        return new ResponseEntity<>(this.managerService.activatePackageInBranch(packageOfBranchesRequest), HttpStatus.ACCEPTED);
    }
    @PutMapping("/deactivate-package-in-branch")
    public ResponseEntity<?> deactivatePackageInBranch(@RequestBody @Valid PackageOfBranchesRequest packageOfBranchesRequest){
        return new ResponseEntity<>(this.managerService.deactivatePackageInBranch(packageOfBranchesRequest), HttpStatus.ACCEPTED);
    }
    @GetMapping("/get-all-packages-by-branch-id/{branchId}")
    public ResponseEntity<?> getAllPackagesByBranchId(@PathVariable Long branchId){
        return new ResponseEntity<>(this.managerService.getAllPackageResponseByBranchId(branchId),HttpStatus.OK);
    }



}
