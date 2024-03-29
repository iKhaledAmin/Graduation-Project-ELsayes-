package com.GP.ELsayes.controller;

import com.GP.ELsayes.model.dto.AddCarRequest;
import com.GP.ELsayes.model.dto.AddServiceToOrderListRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserRequest;
import com.GP.ELsayes.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("")
    public ResponseEntity<?> add(@RequestBody @Valid CustomerRequest customerRequest) {
        return new ResponseEntity<>(this.customerService.add(customerRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<?> update(@RequestBody @Valid CustomerRequest customerRequest,@PathVariable Long customerId){
        return new ResponseEntity<>(this.customerService.update(customerRequest , customerId), HttpStatus.ACCEPTED);
    }

    @PutMapping("/edit-profile/{customerId}")
    public ResponseEntity<?> editProfile(@RequestBody @Valid UserRequest userRequest, @PathVariable Long customerId){
        return new ResponseEntity<>(this.customerService.editProfile(userRequest , customerId), HttpStatus.ACCEPTED);
    }


    @DeleteMapping("/{customerId}")
    public ResponseEntity<?> delete(@PathVariable Long customerId){
        this.customerService.delete(customerId);
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.ACCEPTED);
    }

    @GetMapping("")
    ResponseEntity<?> getAll(){
        return new ResponseEntity<>(this.customerService.getAll(), HttpStatus.OK);
    }
    @GetMapping("/get-by-id/{customerId}")
    public ResponseEntity<?> getById(@PathVariable Long customerId){
        return new ResponseEntity<>(this.customerService.getResponseById(customerId),HttpStatus.OK);
    }

    @PutMapping("/add-car-to-customer")
    public ResponseEntity<?> addCarToCustomer(@RequestBody @Valid AddCarRequest addCarRequest){
        return new ResponseEntity<>(this.customerService.addCarToCustomer(addCarRequest), HttpStatus.ACCEPTED);
    }

    @PutMapping("/add-service-to-order-list")
    public ResponseEntity<?> addServiceToOrderList(@RequestBody @Valid AddServiceToOrderListRequest addServiceToOrderListRequest){
        this.customerService.addServiceToOrderList(addServiceToOrderListRequest);
        return new ResponseEntity<>("Added Successfully", HttpStatus.ACCEPTED);
    }


    @PutMapping("/confirm-order/{customerId}")
    public ResponseEntity<?> confirmOrder(@PathVariable Long customerId){
        this.customerService.confirmOrder(customerId);
        return new ResponseEntity<>("Confirmed Successfully", HttpStatus.ACCEPTED);
    }
}
