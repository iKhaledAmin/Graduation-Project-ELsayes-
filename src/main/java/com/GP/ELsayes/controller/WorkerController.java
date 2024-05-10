package com.GP.ELsayes.controller;

import com.GP.ELsayes.model.dto.CheckOutRequest;
import com.GP.ELsayes.model.dto.FinishTaskRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.EditUserProfileRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.EmployeeChildren.WorkerRequest;

import com.GP.ELsayes.model.dto.SystemUsers.User.UserRequest;
import com.GP.ELsayes.model.dto.RecordVisitationRequest;
import com.GP.ELsayes.service.WorkerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/workers")
public class WorkerController {

    @Autowired
     private WorkerService workerService;

    @GetMapping("/get-worker-by-id/{workerId}")
    public ResponseEntity<?> getWorkerById(@PathVariable Long workerId){
        return new ResponseEntity<>(this.workerService.getResponseById(workerId),HttpStatus.OK);
    }



    @PutMapping("/edit-profile/{workerId}")
    public ResponseEntity<?> editProfile(@RequestBody @Valid EditUserProfileRequest profileRequest, @PathVariable Long workerId){
        return new ResponseEntity<>(this.workerService.editProfile(profileRequest , workerId), HttpStatus.ACCEPTED);
    }

    @PutMapping("/change-worker-status/{workerId}")
    public ResponseEntity<Map<String, String>> changeWorkerStatus(@PathVariable Long workerId){
        this.workerService.changeWorkerStatus(workerId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Changed Successfully");
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @PostMapping("/record-visitation")
    public ResponseEntity<Map<String, String>> recordVisitation(@RequestBody @Valid RecordVisitationRequest customerVisitationsRequest) {
        this.workerService.recordVisitation(
                customerVisitationsRequest.getCarPlateNumber(),
                customerVisitationsRequest.getWorkerId()
        );
        Map<String, String> response = new HashMap<>();
        response.put("message", "Recorded Successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/check-out")
    public ResponseEntity<?> checkOut(@RequestBody @Valid CheckOutRequest checkOutRequest){
        return new ResponseEntity<>(this.workerService.checkOut(checkOutRequest.getCarPlateNumber(),checkOutRequest.getWorkerId()), HttpStatus.ACCEPTED);
    }

    @PutMapping("/finish-task")
    public ResponseEntity<Map<String, String>> finishTask(@RequestBody @Valid FinishTaskRequest finishTaskRequest){
        this.workerService.finishTask(
                finishTaskRequest.getCarPlateNumber(),
                finishTaskRequest.getWorkerId()
        );
        Map<String, String> response = new HashMap<>();
        response.put("message", "Finished Successfully");
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @PostMapping("/generate-free-code/{workerId}")
    public ResponseEntity<Map<String, String>> generateFreeTrialCode(@PathVariable Long workerId){
        this.workerService.generateFreeTrialCode(workerId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Generated Successfully");
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping("get-count-of-visitation-of-branch/{workerId}")
    ResponseEntity<?> getCountOfCurrentVisitationByWorkerId(@PathVariable Long workerId){
        return new ResponseEntity<>(this.workerService.getCountOfCurrentVisitationByWorkerId(workerId), HttpStatus.OK);
    }
    @GetMapping("get-getCapacity-of-branch/{workerId}")
    ResponseEntity<?> getCapacityByBranchId(@PathVariable Long workerId){
        return new ResponseEntity<>(this.workerService.getCapacityByWorkerId(workerId), HttpStatus.OK);
    }



}

