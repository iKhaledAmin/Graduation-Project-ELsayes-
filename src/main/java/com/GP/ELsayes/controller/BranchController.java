package com.GP.ELsayes.controller;

import com.GP.ELsayes.model.dto.BranchRequest;
import com.GP.ELsayes.model.dto.GetVisitationsRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.WorkerResponse;
import com.GP.ELsayes.service.BranchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/branches")
public class BranchController {

    @Autowired
    private BranchService branchService;

    @PostMapping("")
    public ResponseEntity<?> add(@RequestBody @Valid BranchRequest branchRequest) {
        return new ResponseEntity<>(this.branchService.add(branchRequest), HttpStatus.CREATED);
    }


    @PutMapping("/{branchId}")
    public ResponseEntity<?> update(@RequestBody @Valid BranchRequest branchRequest,@PathVariable Long branchId){
        return new ResponseEntity<>(this.branchService.update(branchRequest , branchId), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{branchId}")
    public ResponseEntity<?> delete(@PathVariable Long branchId){
        this.branchService.delete(branchId);
        return new ResponseEntity<>("Deleted successfully", HttpStatus.ACCEPTED);
    }

    @GetMapping("")
    ResponseEntity<?> getAll(){
        return new ResponseEntity<>(this.branchService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/get-by-id/{branchId}")
    public ResponseEntity<?> getById(@PathVariable Long branchId){
        return new ResponseEntity<>(this.branchService.getResponseById(branchId),HttpStatus.OK);
    }

    @GetMapping("/get-by-managerId/{managerId}")
    public ResponseEntity<?> getByManagerId(@PathVariable Long managerId){
        return new ResponseEntity<>(this.branchService.getResponseByManagerId(managerId),HttpStatus.OK);
    }

    @GetMapping("/get-current-customer-in-branch/{branchId}")
    ResponseEntity<?> getAllCurrentVisitationsInBranch(@PathVariable Long branchId){
        return new ResponseEntity<>(this.branchService.getResponseAllCurrentVisitationsInBranch(branchId), HttpStatus.OK);
    }

    @GetMapping("/get-visitations-by-date")
    ResponseEntity<?> getAllVisitationsInBranchByADate(@RequestBody GetVisitationsRequest getVisitationsRequest){
        Long branchId = getVisitationsRequest.getBranchId();
        Date date = getVisitationsRequest.getDate();
        if (date == null) {
            date = java.sql.Date.valueOf(LocalDate.now());
        }
        return new ResponseEntity<>(this.branchService.getResponseAllVisitationsInBranchByADate(branchId, date), HttpStatus.OK);
    }




}
