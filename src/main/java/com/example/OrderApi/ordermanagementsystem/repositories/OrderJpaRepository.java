package com.example.OrderApi.ordermanagementsystem.repositories;

import com.example.OrderApi.ordermanagementsystem.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {

    @Query("select order from orders order where order.orderNumber = :orderNumber")
    Optional<Order> findByOrderNumber(String orderNumber);
}
