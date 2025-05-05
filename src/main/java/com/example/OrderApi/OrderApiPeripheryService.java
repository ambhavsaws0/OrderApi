package com.example.OrderApi;

import com.example.OrderApi.ordermanagementsystem.entities.OrderItem;
import com.example.OrderApi.deliverysystem.EstimatedDeliveryDateProviderService;
import com.example.OrderApi.inventorysystem.InventoryCheckValidator;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class OrderApiPeripheryService {

    private final InventoryCheckValidator inventoryCheckValidator;
    private final EstimatedDeliveryDateProviderService estimatedDeliveryDateProviderService;

    public OrderApiPeripheryService(final InventoryCheckValidator inventoryCheckValidator, final EstimatedDeliveryDateProviderService estimatedDeliveryDateProviderService) {
        this.inventoryCheckValidator = inventoryCheckValidator;
        this.estimatedDeliveryDateProviderService = estimatedDeliveryDateProviderService;
    }

    public boolean productInventoryCheck(final String productSku) {
        return inventoryCheckValidator.isProductAvailableInInventory(productSku);
    }

    public LocalDate getEstimatedDeliveryDate(final OrderItem orderItem) {
        return estimatedDeliveryDateProviderService.retrieveEstimatedDeliveryDate(orderItem);
    }
}
