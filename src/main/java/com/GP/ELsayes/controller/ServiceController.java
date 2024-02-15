package com.GP.ELsayes.controller;

import com.GP.ELsayes.model.dto.ServiceRequest;
import com.GP.ELsayes.service.ServiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ELsayes-services")
public class ServiceController {
    @Autowired
    private ServiceService serviceService;

    @PostMapping("")
    public ResponseEntity<?> add(@RequestBody @Valid ServiceRequest serviceRequest) {
        return new ResponseEntity<>(this.serviceService.add(serviceRequest), HttpStatus.CREATED);
    }

    @GetMapping("/get-by-id/{serviceId}")
    public ResponseEntity<?> getById(@PathVariable Long serviceId){
        return new ResponseEntity<>(this.serviceService.getById(serviceId),HttpStatus.OK);
    }

    @GetMapping("")
    ResponseEntity<?> getAll(){
        return new ResponseEntity<>(this.serviceService.getAll(), HttpStatus.OK);
    }

    @PutMapping("/{serviceId}")
    public ResponseEntity<?> update(@RequestBody @Valid ServiceRequest serviceRequest, @PathVariable Long serviceId){
        return new ResponseEntity<>(this.serviceService.update(serviceRequest , serviceId), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{serviceId}")
    public ResponseEntity<?> delete(@PathVariable Long serviceId){
        this.serviceService.delete(serviceId);
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.ACCEPTED);
    }
}
