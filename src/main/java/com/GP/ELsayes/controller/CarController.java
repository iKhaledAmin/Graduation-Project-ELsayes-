package com.GP.ELsayes.controller;

import com.GP.ELsayes.model.dto.CarRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerRequest;
import com.GP.ELsayes.service.CarService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @PostMapping("")
    public ResponseEntity<?> add(@RequestBody @Valid CarRequest carRequest) {
        return new ResponseEntity<>(this.carService.add(carRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{carId}")
    public ResponseEntity<?> update(@RequestBody @Valid CarRequest carRequest,@PathVariable Long carId){
        return new ResponseEntity<>(this.carService.update(carRequest , carId), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{carId}")
    public ResponseEntity<?> delete(@PathVariable Long carId){
        this.carService.delete(carId);
        return new ResponseEntity<>("Deleted successfully", HttpStatus.ACCEPTED);
    }

    @GetMapping("")
    ResponseEntity<?> getAll(){
        return new ResponseEntity<>(this.carService.getAll(), HttpStatus.OK);
    }
    @GetMapping("/get-by-id/{carId}")
    public ResponseEntity<?> getById(@PathVariable Long carId){
        return new ResponseEntity<>(this.carService.getResponseById(carId),HttpStatus.OK);
    }

}
