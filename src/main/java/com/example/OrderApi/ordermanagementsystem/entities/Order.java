package com.example.OrderApi.ordermanagementsystem.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "orders")
@Getter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long customerId;
    @Setter
    private String orderNumber;
    @Setter
    private double totalAmount;
    private final LocalDateTime createdAt = LocalDateTime.now();
    private final LocalDateTime updatedAt = LocalDateTime.now();
    @Setter
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Getter
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private final List<OrderItem> orderItems = new ArrayList<>();

    public void setOrderItems(final List<OrderItem> orderItems) {
        this.orderItems.addAll(orderItems);
        orderItems.forEach(orderItem -> orderItem.setOrder(this));
    }
}
