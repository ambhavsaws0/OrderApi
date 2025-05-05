package com.example.OrderApi.ordermanagementsystem.validation;

import com.example.OrderApi.ordermanagementsystem.entities.Order;
import com.example.OrderApi.ordermanagementsystem.entities.OrderItem;
import com.example.OrderApi.ordermanagementsystem.exceptionhandler.InvalidOrderDataException;
import org.springframework.stereotype.Component;

@Component
public class OrderDataValidator {

    public void validateOrder(final Order order) {
        if (order.getOrderItems().isEmpty()) {
            throw new InvalidOrderDataException("Order validation failed", "No Order Item added in the Order.");
        }
        if (order.getOrderItems().stream().map(OrderItem::getQuantity).reduce(0, Integer::sum) <= 0) {
            throw new InvalidOrderDataException("Order validation failed", "total quantity of order items is 0");
        }
    }
}
