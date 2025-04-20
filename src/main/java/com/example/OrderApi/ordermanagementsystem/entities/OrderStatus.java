package com.example.OrderApi.ordermanagementsystem.entities;

import java.util.Collections;
import java.util.List;

public enum OrderStatus {
    PENDING, PAID, SHIPPED, CANCELLED;

    public static List<OrderStatus> nextPossibleOrderStatus(final OrderStatus orderStatus) {
        return switch (orderStatus) {
            case PENDING -> List.of(OrderStatus.PAID, OrderStatus.CANCELLED);
            case PAID -> List.of(OrderStatus.SHIPPED, OrderStatus.CANCELLED);
            case SHIPPED -> List.of(OrderStatus.CANCELLED);
            case CANCELLED -> Collections.emptyList();
        };
    }
}
