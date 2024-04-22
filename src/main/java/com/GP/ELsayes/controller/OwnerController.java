package com.GP.ELsayes.controller;


import com.GP.ELsayes.model.dto.BranchRequest;
import com.GP.ELsayes.model.dto.GetVisitationsRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.ManagerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.OwnerRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.OwnerResponse;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserRequest;
import com.GP.ELsayes.service.OwnerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;

@RestController
@CrossOrigin("*")
@RequestMapping("/owners")
public class OwnerController {

    @Autowired
    private  OwnerService ownerService;

    @PostMapping("/register-owner")
    public ResponseEntity<?> registerTheMainOwner(@RequestBody @Valid UserRequest userRequest) {
        return new ResponseEntity<>(this.ownerService.register(userRequest), HttpStatus.CREATED);
    }

    @PostMapping("/add-owner")
    public ResponseEntity<?> add(@RequestBody @Valid OwnerRequest ownerRequest) {
        return new ResponseEntity<>(this.ownerService.add(ownerRequest), HttpStatus.CREATED);
    }

    @PutMapping("/update-owner/{ownerId}")
    public ResponseEntity<?> update(@RequestBody @Valid OwnerRequest ownerRequest,@PathVariable Long ownerId){
        return new ResponseEntity<>(this.ownerService.update(ownerRequest , ownerId), HttpStatus.ACCEPTED);
    }

    @PutMapping("/edit-profile/{ownerId}")
    public ResponseEntity<?> editProfile(@RequestBody @Valid UserRequest userRequest, @PathVariable Long ownerId){
        return new ResponseEntity<>(this.ownerService.editProfile(userRequest , ownerId), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete-owner/{ownerId}")
    public ResponseEntity<?> delete(@PathVariable Long ownerId){
        this.ownerService.delete(ownerId);
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.ACCEPTED);
    }

    @GetMapping("get-all")
    ResponseEntity<?> getAll(){
        return new ResponseEntity<>(this.ownerService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/get-by-id/{ownerId}")
    public ResponseEntity<?> getById(@PathVariable Long ownerId){
        return new ResponseEntity<>(this.ownerService.getResponseById(ownerId),HttpStatus.OK);
    }




    /////

    @PostMapping("/add-manager")
    public ResponseEntity<?> addManager(@RequestBody @Valid ManagerRequest managerRequest) {
        return new ResponseEntity<>(this.ownerService.addManager(managerRequest), HttpStatus.CREATED);
    }

    @PutMapping("/update-manager/{managerId}")
    public ResponseEntity<?> updateManager(@RequestBody ManagerRequest managerRequest,@PathVariable Long managerId){
        return new ResponseEntity<>(this.ownerService.updateManager(managerRequest , managerId), HttpStatus.ACCEPTED);
    }


    @DeleteMapping("/delete-manager/{managerId}")
    public ResponseEntity<?> deleteManager(@PathVariable Long managerId){
        this.ownerService.deleteManager(managerId);
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.ACCEPTED);
    }

    @GetMapping("/get-all-managers")
    ResponseEntity<?> getAllManager(){
        return new ResponseEntity<>(this.ownerService.getAllManager(), HttpStatus.OK);
    }


    @GetMapping("/get-manager-by-id/{managerId}")
    public ResponseEntity<?> getManagerById(@PathVariable Long managerId){
        return new ResponseEntity<>(this.ownerService.getResponseManagerById(managerId),HttpStatus.OK);
    }

    @GetMapping("/get-manager-by-branchId/{branchId}")
    public ResponseEntity<?> getManagerByBranchId(@PathVariable Long branchId){
        return new ResponseEntity<>(this.ownerService.getResponseManagerByBranchId(branchId),HttpStatus.OK);
    }





    @PostMapping("/add-branch")
    public ResponseEntity<?> addBranch(@RequestBody @Valid BranchRequest branchRequest) {
        return new ResponseEntity<>(this.ownerService.addBranch(branchRequest), HttpStatus.CREATED);
    }
    @PutMapping("/update-branch/{branchId}")
    public ResponseEntity<?> updateBranch(@RequestBody @Valid BranchRequest branchRequest,@PathVariable Long branchId){
        return new ResponseEntity<>(this.ownerService.updateBranch(branchRequest , branchId), HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/delete-branch/{branchId}")
    public ResponseEntity<?> deleteBranch(@PathVariable Long branchId){
        this.ownerService.deleteBranch(branchId);
        return new ResponseEntity<>("Deleted successfully", HttpStatus.ACCEPTED);
    }
    @GetMapping("get-all-branches")
    ResponseEntity<?> getAllBranches(){
        return new ResponseEntity<>(this.ownerService.getAllBranches(), HttpStatus.OK);
    }
    @GetMapping("/get-branch-by-id/{branchId}")
    public ResponseEntity<?> getBranchById(@PathVariable Long branchId){
        return new ResponseEntity<>(this.ownerService.getBranchResponseById(branchId), HttpStatus.OK);
    }








}
