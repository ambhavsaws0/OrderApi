package com.example.OrderApi.inventorysystem;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class InventoryCheckValidator {

    public boolean isProductAvailableInInventory(final String productSku) {
        //Todo use inventoryManagement rest endpoint to check the availability
        return new Random().nextBoolean();
    }
}
