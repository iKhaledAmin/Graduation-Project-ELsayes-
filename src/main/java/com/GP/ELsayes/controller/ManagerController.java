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
@CrossOrigin("*")
@RequestMapping("/managers")
public class ManagerController {
    @Autowired
private ManagerService managerService;


    @PutMapping("/edit-profile/{managerId}")
    public ResponseEntity<?> editProfile(@RequestBody @Valid UserRequest userRequest, @PathVariable Long managerId){
        return new ResponseEntity<>(this.managerService.editProfile(userRequest , managerId), HttpStatus.ACCEPTED);
    }


}
