package com.n3c3.rentroom.repository;

import com.n3c3.rentroom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM User WHERE LOWER(phone) = LOWER(:value) OR LOWER(email) = LOWER(:value)", nativeQuery = true)
    User findByPhoneOrEmail(@Param("value") String value);
}
