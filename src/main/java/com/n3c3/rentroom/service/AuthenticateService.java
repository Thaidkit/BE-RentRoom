package com.n3c3.rentroom.service;

import com.n3c3.rentroom.dto.LoginDTO;
import com.n3c3.rentroom.dto.ObjectResponse;
import com.n3c3.rentroom.security.CustomUserDetails;
import com.n3c3.rentroom.security.jwt.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final CustomUserDetailService customUserDetailService;

    public AuthenticateService(
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            CustomUserDetailService customUserDetailService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailService = customUserDetailService;
    }

    public ResponseEntity<?> authenticate(LoginDTO login) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword())
            );
            CustomUserDetails user = customUserDetailService.loadUserByUsername(login.getUsername());
            String jwt = jwtTokenProvider.generateToken(user);

            return ResponseEntity.ok().body(new ObjectResponse(200, "Login successfully!", jwt));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
    }
}
