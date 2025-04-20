package com.example.OrderApi.ordermanagementsystem.businessservice;

import com.example.OrderApi.ordermanagementsystem.entities.Order;
import com.example.OrderApi.ordermanagementsystem.entities.OrderItem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class OrderUtility {
    private OrderUtility() {}

    public static double calculateTotalPrice(final Order order) {
        return order.getOrderItems().stream().mapToDouble(OrderItem::getGrossPrice).sum();
    }

    public static String generateOrderNumber(final long customerId) {
        final String currentDate = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        return String.format("EC-%s-%s-%s", customerId, currentDate, generateRandomInteger());
    }

    public static int generateRandomInteger() {
        return 1000 + new Random().nextInt(900);
    }

    public static double getFinalPrice(OrderItem orderItem) {
        return FinalPriceCalculatorService.calculateFinalPriceOfOrderLineItem(orderItem.getUnitPrice(), orderItem.getDiscountPercent(), orderItem.getTaxPercent());
    }
}
