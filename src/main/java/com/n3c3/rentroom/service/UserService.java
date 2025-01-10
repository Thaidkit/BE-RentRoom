package com.n3c3.rentroom.service;

import com.n3c3.rentroom.dto.ObjectResponse;
import com.n3c3.rentroom.dto.UserDTO;
import com.n3c3.rentroom.entity.User;
import com.n3c3.rentroom.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return userRepository.save(user);

    }


    public ResponseEntity<?> updateUserInfor(Long userId, UserDTO userDTO) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

            // Kiểm tra mật khẩu hiện tại nếu mật khẩu mới được cung cấp
            if (userDTO.getNewPassword() != null && !userDTO.getNewPassword().isEmpty()) {
                if (!passwordEncoder.matches(userDTO.getCurrentPassword(), user.getPassword())) {
                    throw new IllegalArgumentException("Mật khẩu hiện tại không đúng");
                }

                // Kiểm tra mật khẩu mới và xác nhận
                if (!userDTO.getNewPassword().equals(userDTO.getConfirmPassword())) {
                    throw new IllegalArgumentException("Xác nhận mật khẩu mới không trùng khớp");
                }

                // Cập nhật mật khẩu mới
                user.setPassword(passwordEncoder.encode(userDTO.getNewPassword()));
            }

            // Cập nhật các trường khác chỉ khi chúng được cung cấp
            if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()) {
                user.setEmail(userDTO.getEmail());
            }
            if (userDTO.getFullName() != null && !userDTO.getFullName().isEmpty()) {
                user.setFullName(userDTO.getFullName());
            }
            if (userDTO.getPhone() != null && !userDTO.getPhone().isEmpty()) {
                user.setPhone(userDTO.getPhone());
            }

            User updatedUser = userRepository.save(user);

            // Phản hồi thành công
            return ResponseEntity.ok(new ObjectResponse(200, "Updated user information successfully!", updatedUser));
        } catch (IllegalArgumentException e) {
            // Phản hồi lỗi hợp lệ (400)
            return ResponseEntity.status(400).body(new ObjectResponse(400, e.getMessage(), null));
        } catch (Exception e) {
            // Phản hồi lỗi hệ thống (500)
            return ResponseEntity.status(500).body(new ObjectResponse(500, "System error when updating user information!", e.getMessage()));
        }
    }


    public ResponseEntity<ObjectResponse> getUserById(Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));
            return ResponseEntity.ok().body(new ObjectResponse(200, "Fetched user successfully!", user));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ObjectResponse(500, "Error fetching user!", e.getMessage()));
        }
    }



}
