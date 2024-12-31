package com.n3c3.rentroom.controller;

import com.n3c3.rentroom.dto.LoginDTO;

import com.n3c3.rentroom.dto.ObjectResponse;
import com.n3c3.rentroom.entity.User;
import com.n3c3.rentroom.service.AuthenticateService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {

    private final AuthenticateService authenticateService;

    public AuthenticationController(AuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginDTO loginDTO) throws Exception {
        return authenticateService.authenticate(loginDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().body(new ObjectResponse(200, "Logout successfully!", null));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        return authenticateService.register(user);
    }

}
