package com.n3c3.rentroom.repository;

import com.n3c3.rentroom.entity.OTP;
import com.n3c3.rentroom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OTP,Long> {
    @Query("select o from OTP o where o.otp = ?1 and o.user = ?2")
    Optional<OTP> findByOtpAndUser  (Integer otp, User user);

    @Query(value = "select o.* from otp_email o inner join user u on o.user_id = u.id where u.email like :email", nativeQuery = true)
    Optional<OTP> findOtpByEmail(@Param("email") String email);

}
