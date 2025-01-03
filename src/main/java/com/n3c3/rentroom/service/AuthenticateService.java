package com.n3c3.rentroom.service;

import com.n3c3.rentroom.dto.LoginDTO;
import com.n3c3.rentroom.dto.ObjectResponse;
import com.n3c3.rentroom.entity.Role;
import com.n3c3.rentroom.entity.User;
import com.n3c3.rentroom.repository.UserRepository;
import com.n3c3.rentroom.security.CustomUserDetailService;
import com.n3c3.rentroom.security.CustomUserDetails;
import com.n3c3.rentroom.security.jwt.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AuthenticateService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailService customUserDetailService;
    private final UserService userService;
    private final UserRepository userRepository;

    public AuthenticateService(
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            CustomUserDetailService customUserDetailService,
            UserService userService,
            UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailService = customUserDetailService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> authenticate(LoginDTO login) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword())
            );

            CustomUserDetails user = customUserDetailService.loadUserByPhoneOrEmail(login.getUsername());
            String jwt = jwtTokenProvider.generateToken(user);

            return ResponseEntity.ok().body(new ObjectResponse(200, "Login successfully!", jwt));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
    }

    public ResponseEntity<?> register(User user) {
        try {
            if (userRepository.findByPhoneOrEmail(user.getPhone()) == null &&  userRepository.findByPhoneOrEmail(user.getEmail()) == null) {
                user.setPassword(new BCryptPasswordEncoder(10).encode(user.getPassword()));
                user.setRole(Role.USER);
                user.setTotalMoney(0L);
                user.setCreateAt(LocalDate.now());
                user.setModifyAt(LocalDate.now());
                userService.save(user);
            }
            return ResponseEntity.ok().body(new ObjectResponse(200, "User registered successfully", ""));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ObjectResponse(400, "Bad request", e.getMessage()));
        }
    }
}
