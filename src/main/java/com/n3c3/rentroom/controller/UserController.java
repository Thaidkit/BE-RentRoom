package com.n3c3.rentroom.controller;

import com.n3c3.rentroom.dto.UserDTO;
import com.n3c3.rentroom.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/userInfor")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // API lấy thông tin user theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // API cập nhật thông tin user
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserInfor(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return userService.updateUserInfor(id, userDTO);
    }
}
