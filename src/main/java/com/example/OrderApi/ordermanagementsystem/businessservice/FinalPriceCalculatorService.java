package com.example.OrderApi.ordermanagementsystem.businessservice;

import org.springframework.stereotype.Component;

@Component
public class FinalPriceCalculatorService {

    public static double calculateFinalPriceOfOrderLineItem(final double unitPrice, final double discountPercent, final double taxPercent) {
        final double tax = calculateTax(taxPercent, unitPrice);
        final double discount = calculateDiscount(discountPercent, unitPrice);
        return (unitPrice + tax) - discount;
    }

    private static double calculateDiscount(final double discountPercent, final double unitPrice) {
        return (discountPercent * unitPrice) / 100;
    }

    private static double calculateTax(final double taxPercent, final double unitPrice) {
        return (taxPercent * unitPrice) / 100;
    }
}
