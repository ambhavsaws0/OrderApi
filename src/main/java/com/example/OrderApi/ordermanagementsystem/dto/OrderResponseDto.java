package com.example.OrderApi.ordermanagementsystem.dto;

import com.example.OrderApi.ordermanagementsystem.entities.OrderStatus;

import java.util.List;

public record OrderResponseDto(String OrderNumber, long customerNumber, OrderStatus orderStatus,
                               List<OrderItemResponseDto> orderItems) {
}
