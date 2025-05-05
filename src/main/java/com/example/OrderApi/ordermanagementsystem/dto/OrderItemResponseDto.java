package com.example.OrderApi.ordermanagementsystem.dto;

import com.example.OrderApi.ordermanagementsystem.entities.OrderItemStatus;

public record OrderItemResponseDto(String productName, int quantity, OrderItemStatus orderItemStatus) {
}
