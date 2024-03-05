package com.GP.ELsayes.controller;

import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.ManagerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserRequest;
import com.GP.ELsayes.service.ManagerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/managers")
public class ManagerController {

    @Autowired
private ManagerService managerService;


    @PostMapping("")
    public ResponseEntity<?> add(@RequestBody @Valid ManagerRequest managerRequest) {
        return new ResponseEntity<>(this.managerService.add(managerRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{managerId}")
    public ResponseEntity<?> update(@RequestBody ManagerRequest managerRequest,@PathVariable Long managerId){
        return new ResponseEntity<>(this.managerService.update(managerRequest , managerId), HttpStatus.ACCEPTED);
    }

    @PutMapping("/edit-profile/{managerId}")
    public ResponseEntity<?> editProfile(@RequestBody @Valid UserRequest userRequest, @PathVariable Long managerId){
        return new ResponseEntity<>(this.managerService.editProfile(userRequest , managerId), HttpStatus.ACCEPTED);
    }


    @DeleteMapping("/{managerId}")
    public ResponseEntity<?> delete(@PathVariable Long managerId){
        this.managerService.delete(managerId);
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.ACCEPTED);
    }

    @GetMapping("")
    ResponseEntity<?> getAll(){
        return new ResponseEntity<>(this.managerService.getAll(), HttpStatus.OK);
    }


    @GetMapping("/get-by-id/{managerId}")
    public ResponseEntity<?> getById(@PathVariable Long managerId){
        return new ResponseEntity<>(this.managerService.getResponseById(managerId),HttpStatus.OK);
    }

    @GetMapping("/get-by-branchId/{branchId}")
    public ResponseEntity<?> getByBranchId(@PathVariable Long branchId){
        return new ResponseEntity<>(this.managerService.getResponseByBranchId(branchId),HttpStatus.OK);
    }



}
