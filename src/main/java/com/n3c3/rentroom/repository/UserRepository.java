package com.n3c3.rentroom.repository;

import com.n3c3.rentroom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM User WHERE username LIKE %:username%", nativeQuery = true)
    User findByUsername(String username);
}
