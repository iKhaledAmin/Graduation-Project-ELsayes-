package com.GP.ELsayes.controller;

import com.GP.ELsayes.model.dto.CheckOutRequest;
import com.GP.ELsayes.model.dto.FinishTaskRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.WorkerRequest;

import com.GP.ELsayes.model.dto.SystemUsers.User.UserRequest;
import com.GP.ELsayes.model.dto.RecordVisitationRequest;
import com.GP.ELsayes.service.WorkerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/workers")
public class WorkerController {

    @Autowired
     private WorkerService workerService;


    @PutMapping("/edit-profile/{workerId}")
    public ResponseEntity<?> editProfile(@RequestBody @Valid UserRequest userRequest, @PathVariable Long workerId){
        return new ResponseEntity<>(this.workerService.editProfile(userRequest , workerId), HttpStatus.ACCEPTED);
    }

    @PostMapping("/record-visitation")
    public ResponseEntity<?> recordVisitation(@RequestBody @Valid RecordVisitationRequest customerVisitationsRequest) {
        this.workerService.recordVisitation(
                customerVisitationsRequest.getCarPlateNumber(),
                customerVisitationsRequest.getBranchId()
        );
        return new ResponseEntity<>("Recorded Successfully", HttpStatus.CREATED);
    }
    @PutMapping("/check-out")
    public ResponseEntity<?> checkOut(@RequestBody @Valid CheckOutRequest checkOutRequest){
        return new ResponseEntity<>(this.workerService.checkOut(checkOutRequest.getCarPlateNumber(),checkOutRequest.getWorkerId()), HttpStatus.ACCEPTED);
    }

    @PutMapping("/finish-task")
    public ResponseEntity<?> finishTask(@RequestBody @Valid FinishTaskRequest finishTaskRequest){
      this.workerService.finishTask(finishTaskRequest.getCarPlateNumber(),finishTaskRequest.getWorkerId()) ;
      return new ResponseEntity<>("Finished Successfully",HttpStatus.ACCEPTED);
    }

}

