package com.example.OrderApi.ordermanagementsystem.entities;

import jakarta.persistence.Embeddable;

@Embeddable
public record ShippingDetail(float weight, String dimensions, String courier) {
}
