package com.GP.ELsayes.controller;

import com.GP.ELsayes.model.dto.FreeTrialCodeRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.CustomerRequest;
import com.GP.ELsayes.service.FreeTrialCodeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/free-trail")
public class FreeTrialCodeController {

    @Autowired
    private FreeTrialCodeService freeTrialCodeService;

    @PostMapping("/{workerId}")
    public ResponseEntity<?> generate(@PathVariable Long workerId) {
        return new ResponseEntity<>(freeTrialCodeService.generateCode(workerId), HttpStatus.CREATED);
    }

    @PutMapping("/{codeId}")
    public ResponseEntity<?> update(@RequestBody @Valid FreeTrialCodeRequest freeTrialCodeRequest,@PathVariable Long codeId){
        return new ResponseEntity<>(this.freeTrialCodeService.update(freeTrialCodeRequest , codeId), HttpStatus.ACCEPTED);
    }
//    @PutMapping("/apply-code")
//    public ResponseEntity<?> applyCode(@RequestBody @Valid FreeTrialCodeRequest freeTrialCodeRequest){
//        return new ResponseEntity<>(this.freeTrialCodeService.applyCode(freeTrialCodeRequest), HttpStatus.ACCEPTED);
//    }
}
