package com.example.OrderApi;

import com.example.OrderApi.ordermanagementsystem.entities.OrderItem;
import com.example.OrderApi.deliverysystem.EstimatedDeliveryDateProviderService;
import com.example.OrderApi.inventorysystem.InventoryCheckValidator;
import com.example.OrderApi.ordermanagementsystem.kafka.event.PaymentEvent;
import com.example.OrderApi.ordermanagementsystem.kafka.producer.PaymentEventProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class OrderApiPeripheryService {

    private final InventoryCheckValidator inventoryCheckValidator;
    private final EstimatedDeliveryDateProviderService estimatedDeliveryDateProviderService;
    private final PaymentEventProducer paymentEventProducer;
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderApiPeripheryService.class);

    public OrderApiPeripheryService(final InventoryCheckValidator inventoryCheckValidator, final EstimatedDeliveryDateProviderService estimatedDeliveryDateProviderService, final PaymentEventProducer paymentEventProducer) {
        this.inventoryCheckValidator = inventoryCheckValidator;
        this.estimatedDeliveryDateProviderService = estimatedDeliveryDateProviderService;
        this.paymentEventProducer = paymentEventProducer;
    }

    public boolean productInventoryCheck(final String productSku) {
        return inventoryCheckValidator.isProductAvailableInInventory(productSku);
    }

    public LocalDate getEstimatedDeliveryDate(final OrderItem orderItem) {
        return estimatedDeliveryDateProviderService.retrieveEstimatedDeliveryDate(orderItem);
    }

    public void sendPaymentEvent(final PaymentEvent paymentEvent) {
        try {
            paymentEventProducer.sendPaymentEvent(paymentEvent);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error occurred in sending the Order: {} to Payment System: {}", paymentEvent.orderNumber(), e.getMessage());
        }
    }
}
