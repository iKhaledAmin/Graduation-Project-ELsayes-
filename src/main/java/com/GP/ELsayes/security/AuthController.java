package com.GP.ELsayes.security;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


//@CrossOrigin("*")
@RequestMapping("/auth")
@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;


//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//        return new ResponseEntity<>(userDetails, HttpStatus.ACCEPTED);
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        //System.out.println("Attempting to authenticate user: " + request.getUserName());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return new ResponseEntity<>(userDetails, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            //System.out.println("Authentication failed: " + e.getMessage());
            return new ResponseEntity<>("Authentication failed: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}