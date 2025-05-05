package com.example.OrderApi.ordermanagementsystem.entities;

import jakarta.persistence.Embeddable;
import lombok.Builder;

@Embeddable
@Builder
public record ShippingDetail(float weight, String dimensions, String courier) {
}
