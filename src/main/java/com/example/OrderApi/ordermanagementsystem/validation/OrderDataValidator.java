package com.example.OrderApi.ordermanagementsystem.validation;

import com.example.OrderApi.ordermanagementsystem.entities.Order;
import com.example.OrderApi.ordermanagementsystem.entities.OrderItem;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class OrderDataValidator {

    public List<String> validateOrder(final Order order) {
        if (order.getOrderItems().isEmpty()) {
            return List.of("There is no Order Item added in this Order.");
        }
        if (order.getOrderItems().stream().map(OrderItem::getQuantity).reduce(0, Integer::sum) <= 0) {
            return List.of("The unit price of the Order Items is 0");
        }
        return Collections.emptyList();
    }
}
