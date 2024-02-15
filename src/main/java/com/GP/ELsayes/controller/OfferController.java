package com.GP.ELsayes.controller;

import com.GP.ELsayes.model.dto.OfferRequest;
import com.GP.ELsayes.model.dto.SystemUsers.User.UserChildren.OwnerRequest;
import com.GP.ELsayes.service.OfferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/offers")
public class OfferController {

    @Autowired
    private OfferService offerService;

    @PostMapping("")
    public ResponseEntity<?> add(@RequestBody @Valid OfferRequest offerRequest) {
        return new ResponseEntity<>(this.offerService.add(offerRequest), HttpStatus.CREATED);
    }

    @GetMapping("/get-by-id/{offerId}")
    public ResponseEntity<?> getById(@PathVariable Long offerId){
        return new ResponseEntity<>(this.offerService.getById(offerId),HttpStatus.OK);
    }

    @GetMapping("")
    ResponseEntity<?> getAll(){
        return new ResponseEntity<>(this.offerService.getAll(), HttpStatus.OK);
    }

    @PutMapping("/{offerId}")
    public ResponseEntity<?> update(@RequestBody @Valid OfferRequest offerRequest,@PathVariable Long offerId){
        return new ResponseEntity<>(this.offerService.update(offerRequest , offerId), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{offerId}")
    public ResponseEntity<?> delete(@PathVariable Long offerId){
        this.offerService.delete(offerId);
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.ACCEPTED);
    }

}