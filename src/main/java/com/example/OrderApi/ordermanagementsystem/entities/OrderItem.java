package com.example.OrderApi.ordermanagementsystem.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long productId; // Identifier of the product being purchased.
    private String productName;
    private int quantity; // Number of units of the product ordered.
    private double unitPrice;
    @Setter
    private double grossPrice; // Total cost of this item (unitPrice * quantity)
    @Setter
    private LocalDate deliveryDate;

    //Optional fields
    private String productSku; // SKU (Stock Keeping Unit) of the product for inventory tracking.
    private double discountPercent; // Discount applied to this item (either a flat value or percentage).
    private UUID variantId; // Reference to a specific variant of the product (e.g., size or color).
    private double taxPercent; // Tax amount applied to this item
    @Setter
    private double finalPrice; // Final price of the item after discounts and taxes (totalPrice + tax - discount).

    //Audit and Status Fields
    @Setter
    @Enumerated(EnumType.STRING)
    private OrderItemStatus status;
    private final LocalDateTime createdAt = LocalDateTime.now();
    private final LocalDateTime updatedAt = LocalDateTime.now();

    //Additional Fields
    private long warehouseId;

    @Embedded
    private ShippingDetail shippingDetail; //Information about how this item will be shipped (e.g., dimensions, weight).
    private boolean isGift;
    private String giftMessage;
    private boolean returnEligibility; //Indicates whether an item can be returned;
    private int loyaltyPointsEarned;

    @Setter
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    public OrderItem() {}

}
