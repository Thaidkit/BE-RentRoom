package com.n3c3.rentroom.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @GetMapping()
    public ResponseEntity<?> helloWorld() {
        return ResponseEntity.ok("Hello World");
    }
}
