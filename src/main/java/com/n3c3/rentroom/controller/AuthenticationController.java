package com.n3c3.rentroom.controller;

import com.n3c3.rentroom.dto.LoginDTO;

import com.n3c3.rentroom.service.AuthenticateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {

    private final AuthenticateService authenticateService;

    public AuthenticationController(AuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }

    @GetMapping()
    public ResponseEntity<?> helloWorld() {
        return ResponseEntity.ok("Hello World");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginDTO loginDTO) throws Exception {
        return authenticateService.authenticate(loginDTO);
    }



}
