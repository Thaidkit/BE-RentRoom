package com.n3c3.rentroom.service;

import com.n3c3.rentroom.dto.*;
import com.n3c3.rentroom.entity.OTP;
import com.n3c3.rentroom.entity.Role;
import com.n3c3.rentroom.entity.User;
import com.n3c3.rentroom.repository.OtpRepository;
import com.n3c3.rentroom.repository.UserRepository;
import com.n3c3.rentroom.security.CustomUserDetailService;
import com.n3c3.rentroom.security.CustomUserDetails;
import com.n3c3.rentroom.security.jwt.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;

@Service
public class AuthenticateService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailService customUserDetailService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final EmailService emailService;

    public AuthenticateService(
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            CustomUserDetailService customUserDetailService,
            UserService userService,
            UserRepository userRepository, OtpRepository otpRepository, EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailService = customUserDetailService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.emailService = emailService;
    }

    public ResponseEntity<?> authenticate(LoginDTO login) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword())
            );

            CustomUserDetails user = customUserDetailService.loadUserByPhoneOrEmail(login.getUsername());
            String jwt = jwtTokenProvider.generateToken(user);

            return ResponseEntity.ok().body(new ObjectResponse(200, "Login successfully!", jwt));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ObjectResponse(401, "Incorrect username or password", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ObjectResponse(500, "Authentication failed: " + e.getMessage(), ""));
        }
    }


    public ResponseEntity<?> register(UserCreateDTO userCreateDTO) {
        try {
            User user = new User();
                if (userRepository.findByPhoneOrEmail(userCreateDTO.getPhone()) == null && userRepository.findByPhoneOrEmail(userCreateDTO.getEmail()) == null) {
                user.setPhone(userCreateDTO.getPhone());
                user.setEmail(userCreateDTO.getEmail());
                user.setFullName(userCreateDTO.getFullName());
                user.setImage(userCreateDTO.getImage());
                user.setPassword(new BCryptPasswordEncoder(10).encode(userCreateDTO.getPassword()));
                user.setRole(Role.USER);
                user.setTotalMoney(0L);
                user.setCreateAt(LocalDate.now());
                user.setModifyAt(LocalDate.now());
                userService.save(user);
                    if (otpRepository.findOtpByEmail(user.getEmail()).isPresent()) {
                        OTP otp = otpRepository.findOtpByEmail(user.getEmail()).get();
                        otpRepository.deleteById(otp.getId());
                        sendOtp(user);
                    } else
                        sendOtp(user);
                return ResponseEntity.ok().body(new ObjectResponse(200, "User registered successfully", user));
            }
                return ResponseEntity.ok().body(new ObjectResponse(500, "User registered fail", "Email or phone existed"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ObjectResponse(400, "Bad request", e.getMessage()));
        }
    }
    public ResponseEntity<ObjectResponse> verifyAndActivateAccount(Integer otp, String email) {
        try {
            // Kiểm tra email hợp lệ
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Vui lòng nhập email hợp lệ!"));

            // Kiểm tra OTP hợp lệ
            OTP otpob = otpRepository.findByOtpAndUser(otp, user)
                    .orElseThrow(() -> new RuntimeException("OTP không hợp lệ cho email " + email));

            // Kiểm tra OTP đã hết hạn
            if (otpob.getExpirationTime().before(Date.from(Instant.now()))) {
                otpRepository.deleteById(otpob.getId());
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                        .body(new ObjectResponse(417, "OTP đã hết hạn!", null));
            }

            // Cập nhật trạng thái tài khoản thành 'active' (đã xác minh)
            user.setActived(true);
            userRepository.save(user);

            // Xóa OTP sau khi xác minh thành công
            otpRepository.deleteById(otpob.getId());

            return ResponseEntity.ok().body(new ObjectResponse(200, "Tài khoản đã được kích hoạt thành công!", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ObjectResponse(500, "Lỗi khi xử lý yêu cầu!", e.getMessage()));
        }
    }

    public ResponseEntity<?> resendOtp(String email) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found!"));

            if (otpRepository.findOtpByEmail(email).isPresent()) {
                OTP otp = otpRepository.findOtpByEmail(email).get();
                otpRepository.deleteById(otp.getId());
                sendOtp(user);
            } else
                sendOtp(user);
            return ResponseEntity.ok().body(new ObjectResponse(200, "OTP resend successful", ""));
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(new ObjectResponse(500, "OTP resend fail: " + e.getMessage(), ""));
        }
    }

    private void sendOtp(User user) {
        int genOtp = otpGeneration();

        OTP otp = new OTP();
        otp.setOtp(genOtp);
        otp.setUser(user);
        otp.setExpirationTime(new Date(System.currentTimeMillis() + 120 * 1000)); // 2 phút

        otpRepository.save(otp);

        MailBody mailBody = new MailBody(user.getEmail(),
                "OTP Verification",
                "Đây là mã OTP để kích hoạt tài khoản của bạn: " + genOtp);
        emailService.sendSimpleMessage(mailBody);
    }

    private Integer otpGeneration(){
        Random random = new Random();
        return random.nextInt(100_000,999_999);
    }
}
