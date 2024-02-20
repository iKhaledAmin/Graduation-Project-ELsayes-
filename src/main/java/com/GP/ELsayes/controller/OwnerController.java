package com.GP.ELsayes.controller;


import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.OwnerRequest;
import com.GP.ELsayes.service.OwnerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owners")
public class OwnerController {

    @Autowired
    private  OwnerService ownerService;

    @PostMapping("")
    public ResponseEntity<?> add(@RequestBody @Valid OwnerRequest ownerRequest) {
        return new ResponseEntity<>(this.ownerService.add(ownerRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{ownerId}")
    public ResponseEntity<?> update(@RequestBody @Valid OwnerRequest ownerRequest,@PathVariable Long ownerId){
        return new ResponseEntity<>(this.ownerService.update(ownerRequest , ownerId), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{ownerId}")
    public ResponseEntity<?> delete(@PathVariable Long ownerId){
        this.ownerService.delete(ownerId);
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.ACCEPTED);
    }

    @GetMapping("")
    ResponseEntity<?> getAll(){
        return new ResponseEntity<>(this.ownerService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/get-by-id/{ownerId}")
    public ResponseEntity<?> getById(@PathVariable Long ownerId){
        return new ResponseEntity<>(this.ownerService.getResponseById(ownerId),HttpStatus.OK);
    }



}
