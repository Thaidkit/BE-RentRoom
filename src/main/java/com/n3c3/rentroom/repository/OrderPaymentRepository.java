package com.n3c3.rentroom.repository;

import com.n3c3.rentroom.entity.OrderPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderPaymentRepository extends JpaRepository<OrderPayment, Long> {
}
