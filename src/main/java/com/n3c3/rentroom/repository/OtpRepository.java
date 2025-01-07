package com.n3c3.rentroom.repository;

import com.n3c3.rentroom.entity.OTP;
import com.n3c3.rentroom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OTP,Long> {
    @Query("select o from OTP o where o.otp = ?1 and o.user = ?2")
    Optional<OTP> findByOtpAndUser  (Integer otp, User user);
}
