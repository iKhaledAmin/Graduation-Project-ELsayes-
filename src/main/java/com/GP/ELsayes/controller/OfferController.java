package com.GP.ELsayes.controller;

import com.GP.ELsayes.model.dto.OfferRequest;
import com.GP.ELsayes.model.dto.relations.OffersOfBranchesRequest;
import com.GP.ELsayes.model.dto.relations.ServicesOfBranchesRequest;
import com.GP.ELsayes.service.OfferService;
import com.GP.ELsayes.service.ServiceService;
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

    @Autowired
    private ServiceService serviceService;

    @PostMapping("")
    public ResponseEntity<?> add(@RequestBody @Valid OfferRequest offerRequest) {
        return new ResponseEntity<>(this.offerService.add(offerRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{offerId}")
    public ResponseEntity<?> update(@RequestBody @Valid OfferRequest offerRequest,@PathVariable Long offerId){
        return new ResponseEntity<>(this.offerService.update(offerRequest , offerId), HttpStatus.ACCEPTED);
    }


    @GetMapping("/get-by-id/{offerId}")
    public ResponseEntity<?> getById(@PathVariable Long offerId){
        return new ResponseEntity<>(this.offerService.getResponseById(offerId),HttpStatus.OK);
    }

    @DeleteMapping("/{offerId}")
    public ResponseEntity<?> delete(@PathVariable Long offerId){
        this.offerService.delete(offerId);
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.ACCEPTED);
    }

    @GetMapping("")
    ResponseEntity<?> getAll(){
        return new ResponseEntity<>(this.offerService.getAll(), HttpStatus.OK);
    }



    @PostMapping("/add-offer-to-branch")
    public ResponseEntity<?> addOfferToBranch(@RequestBody @Valid OffersOfBranchesRequest offersOfBranchesRequest) {
        return new ResponseEntity<>(this.offerService.addOfferToBranch(offersOfBranchesRequest), HttpStatus.CREATED);
    }

    @PutMapping("/activate-offer-in-branch")
    public ResponseEntity<?> activateServiceInBranch(@RequestBody @Valid OffersOfBranchesRequest offersOfBranchesRequest){
        return new ResponseEntity<>(this.offerService.activateOfferInBranch(offersOfBranchesRequest), HttpStatus.ACCEPTED);
    }

    @PutMapping("/deactivate-offer-in-branch")
    public ResponseEntity<?> deactivateServiceInBranch(@RequestBody @Valid OffersOfBranchesRequest offersOfBranchesRequest){
        return new ResponseEntity<>(this.offerService.deactivateOfferInBranch(offersOfBranchesRequest), HttpStatus.ACCEPTED);
    }

    @GetMapping("/get-all-by-branch-id/{branchId}")
    public ResponseEntity<?> getAllByBranchId(@PathVariable Long branchId){
        return new ResponseEntity<>(this.offerService.getResponseAllBranchId(branchId),HttpStatus.OK);
    }
}
