package com.example.OrderApi.ordermanagementsystem.kafka.event;

public record PaymentEvent(String orderNumber, double totalAmount) {
}
