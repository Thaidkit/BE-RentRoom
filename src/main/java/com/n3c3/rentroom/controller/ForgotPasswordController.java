package com.n3c3.rentroom.controller;

import com.n3c3.rentroom.dto.ChangePassword;
import com.n3c3.rentroom.dto.MailBody;
import com.n3c3.rentroom.dto.ObjectResponse;
import com.n3c3.rentroom.entity.OTP;
import com.n3c3.rentroom.entity.User;
import com.n3c3.rentroom.repository.OtpRepository;
import com.n3c3.rentroom.repository.UserRepository;
import com.n3c3.rentroom.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("/api/v1/forgotPassword")
public class ForgotPasswordController {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final OtpRepository otpRepository;


    public ForgotPasswordController(UserRepository userRepository, EmailService emailService, OtpRepository otpRepository) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.otpRepository = otpRepository;

    }

    //send mail for email verification
    @PostMapping("/verifyMail/{email}")
    public ResponseEntity<ObjectResponse> verifyEmail(@PathVariable String email) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Vui lòng nhập email hợp lệ!"));

            int genOtp = otpGeneration();

            MailBody mailBody = new MailBody(email, "OTP Verification", "Đây là OTP cho yêu cầu Quên mật khẩu của bạn: " + genOtp);

            OTP otp = new OTP();
            otp.setOtp(genOtp);
            otp.setUser(user);
            otp.setExpirationTime(new Date(System.currentTimeMillis() + 120 * 1000));

            emailService.sendSimpleMessage(mailBody);
            otpRepository.save(otp);

            return ResponseEntity.ok().body(new ObjectResponse(200, "Đã gửi email để xác minh!", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ObjectResponse(500, "Lỗi khi gửi OTP qua email!", e.getMessage()));
        }
    }

    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<?> verifyOtp(@PathVariable Integer otp, @PathVariable String email){
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Vui lòng nhập email hợp lệ!"));
            OTP otpob =otpRepository.findByOtpAndUser(otp,user).orElseThrow(()->new RuntimeException("OTP không hợp lệ cho email " + email));

            if (otpob.getExpirationTime().before(Date.from(Instant.now()))){
                otpRepository.deleteById(otpob.getId());
                return ResponseEntity.badRequest().body(new ObjectResponse(400,"OTP đã hết hạn!", ""));
            }

            return ResponseEntity.ok().body(new ObjectResponse(200,"OTP đã xác minh", ""));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ObjectResponse(500, e.getMessage(), ""));
        }
    }

    @PostMapping("/changePassword/{email}")
    public ResponseEntity<?> changePasswordHandle(@RequestBody ChangePassword changePassword,
                                                       @PathVariable String email){
        try {
            if(!Objects.equals(changePassword.password(), changePassword.repeatPassword())){
                return ResponseEntity.badRequest().body(new ObjectResponse(400, "Retyping password!", ""));
            }
            String encodedPassword = new BCryptPasswordEncoder(10).encode(changePassword.password());
            userRepository.updatePassword(email, encodedPassword);
            return ResponseEntity.ok().body(new ObjectResponse(200, "Change password successfully!", ""));
        }  catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ObjectResponse(500, "Error changing password", e.getMessage()));
        }
    }

//    @PostMapping("/verifyAndChangePassword/{otp}/{email}")
//    public ResponseEntity<ObjectResponse> verifyAndChangePassword(@PathVariable Integer otp,
//                                                                  @PathVariable String email,
//                                                                  @RequestBody ChangePassword changePassword) {
//        try {
//            // Kiểm tra email hợp lệ
//            User user = userRepository.findByEmail(email)
//                    .orElseThrow(() -> new UsernameNotFoundException("Vui lòng nhập email hợp lệ!"));
//
//            // Kiểm tra OTP hợp lệ
//            OTP otpob = otpRepository.findByOtpAndUser(otp, user)
//                    .orElseThrow(() -> new RuntimeException("OTP không hợp lệ cho email " + email));
//
//            if (otpob.getExpirationTime().before(Date.from(Instant.now()))) {
//                otpRepository.deleteById(otpob.getId());
//                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
//                        .body(new ObjectResponse(417, "OTP đã hết hạn!", null));
//            }
//
//            // Kiểm tra mật khẩu và xác nhận
//            if (!Objects.equals(changePassword.password(), changePassword.repeatPassword())) {
//                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
//                        .body(new ObjectResponse(417, "Vui lòng nhập lại mật khẩu!", null));
//            }
//
//            // Mã hóa mật khẩu mới và lưu vào cơ sở dữ liệu
//            String encodedPassword = new BCryptPasswordEncoder(10).encode(changePassword.password());
//            userRepository.updatePassword(email, encodedPassword);
//
//            // Xóa OTP sau khi hoàn thành
//            otpRepository.deleteById(otpob.getId());
//
//            return ResponseEntity.ok().body(new ObjectResponse(200, "Đổi mật khẩu thành công!", null));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ObjectResponse(500, "Lỗi khi xử lý yêu cầu!", e.getMessage()));
//        }
//    }

    private Integer otpGeneration(){
        Random random = new Random();
        return random.nextInt(100_000,999_999);
    }
}
