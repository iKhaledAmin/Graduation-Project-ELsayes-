package com.GP.ELsayes.controller;

import com.GP.ELsayes.model.dto.*;
import com.GP.ELsayes.model.dto.SystemUsers.User.EditUserProfileRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserRequest;
import com.GP.ELsayes.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid CustomerRequest customerRequest) {
        return new ResponseEntity<>(this.customerService.register(customerRequest), HttpStatus.CREATED);
    }

    @GetMapping("/get-customer-by-id/{customerId}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long customerId){
        return new ResponseEntity<>(this.customerService.getResponseById(customerId),HttpStatus.OK);
    }

    @PutMapping("/edit-profile/{customerId}")
    public ResponseEntity<?> editProfile(@RequestBody @Valid EditUserProfileRequest profileRequest, @PathVariable Long customerId){
        return new ResponseEntity<>(this.customerService.editProfile(profileRequest , customerId), HttpStatus.ACCEPTED);
    }




    @PutMapping("/add-car")
    public ResponseEntity<?> addCarToCustomer(@RequestBody @Valid AddCarRequest addCarRequest){
        return new ResponseEntity<>(this.customerService.addCarToCustomer(addCarRequest), HttpStatus.ACCEPTED);
    }



    @PutMapping("/update-car/{carId}")
    public ResponseEntity<?> updateCar(@RequestBody @Valid CarRequest carRequest, @PathVariable Long carId){
        return new ResponseEntity<>(this.customerService.updateCar(carRequest , carId), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete-car/{carId}")
    public ResponseEntity<Map<String, String>> deleteCar(@PathVariable Long carId){
        this.customerService.deleteCar(carId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping("/get-car-by-id/{carId}")
    public ResponseEntity<?> getCarById(@PathVariable Long carId){
        return new ResponseEntity<>(this.customerService.getCarById(carId),HttpStatus.OK);
    }

    @GetMapping("/get-car-by-customer-id/{customerId}")
    public ResponseEntity<?> getCarByCustomerId(@PathVariable Long customerId){
        return new ResponseEntity<>(this.customerService.getCarByCustomerId(customerId),HttpStatus.OK);
    }

    @GetMapping("/get-all-cleaning-services")
    ResponseEntity<?> getAllCleaningServices(){
        return new ResponseEntity<>(this.customerService.getAllCleaningServices(), HttpStatus.OK);
    }

    @GetMapping("/get-all-maintenance-services")
    ResponseEntity<?> getAllMaintenanceServices(){
        return new ResponseEntity<>(this.customerService.getAllMaintenanceServices(), HttpStatus.OK);
    }

    @GetMapping("/get-all-take-away-services")
    ResponseEntity<?> getAllTakeAwayServices(){
        return new ResponseEntity<>(this.customerService.getAllTakeAwayServices(), HttpStatus.OK);
    }

    @GetMapping("/get-service-by-id-and-branch-by-id")
    public ResponseEntity<?> getServiceByIdAndBranchId(@RequestBody @Valid GetServiceRequest getServiceRequest) {
        return new ResponseEntity<>(this.customerService.getServiceByIdAndBranchId(getServiceRequest.getServiceId(),getServiceRequest.getBranchId()), HttpStatus.OK);
    }



    @PutMapping("/add-service-to-order-list")
    public ResponseEntity<Map<String, String>> addServiceToOrderList(@RequestBody @Valid AddServiceToOrderListRequest addServiceToOrderListRequest){
        this.customerService.addServiceToOrderList(addServiceToOrderListRequest);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Added Successfully");
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete-service-from-order-list/{serviceId}")
    public ResponseEntity<Map<String, String>> deleteServiceFromOrderList(@PathVariable Long serviceId){
        this.customerService.deleteServiceFromOrderList(serviceId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Deleted Successfully");
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }


    @GetMapping("/get-package-by-id-and-branch-by-id")
    public ResponseEntity<?> getPackageByIdAndBranchId(@RequestBody @Valid GetPackageRequest getPackageRequest) {
        return new ResponseEntity<>(this.customerService.getPackageByIdAndBranchId(getPackageRequest.getPackageId(),getPackageRequest.getBranchId()), HttpStatus.OK);
    }

    @PutMapping("/add-package-to-order-list")
    public ResponseEntity<Map<String, String>> addPackageToOrderList(@RequestBody @Valid AddPackageToOrderListRequest addPackageToOrderListRequest){
        this.customerService.addPackageToOrderList(addPackageToOrderListRequest);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Added Successfully");
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete-package-from-order-list/{packageId}")
    public ResponseEntity<Map<String, String>> deletePackageFromOrderList(@PathVariable Long packageId){
        this.customerService.deletePackageFromOrderList(packageId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Deleted Successfully");
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/clear-order-list/{customerId}")
    public ResponseEntity<Map<String, String>> clearOrderList(@PathVariable Long customerId){
        this.customerService.clearOrderList(customerId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Deleted Successfully");
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @PutMapping("/confirm-order/{customerId}")
    public ResponseEntity<Map<String, String>> confirmOrder(@PathVariable Long customerId){
        this.customerService.confirmOrder(customerId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Confirmed Successfully");
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
    @GetMapping("/get-non-confirm-order/{customerId}")
    public ResponseEntity<?> getUnConfirmedOrder(@PathVariable Long customerId){
        return new ResponseEntity<>(this.customerService.getUnConfirmedOrder(customerId),HttpStatus.OK);
    }

    @GetMapping("/get-progress-confirm-order/{customerId}")
    public ResponseEntity<?> getProgressOfConfirmedOrder(@PathVariable Long customerId){
        return new ResponseEntity<>(this.customerService.getProgressOfConfirmedOrder(customerId),HttpStatus.OK);
    }
}
