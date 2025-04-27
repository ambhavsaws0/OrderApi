package com.example.OrderApi.inventorysystem;

import org.springframework.stereotype.Component;

@Component
public class InventoryCheckValidator {

    public boolean isProductAvailableInInventory(final String productSku) {
        //Todo use inventoryManagement rest endpoint to check the availability, using a random check to simulate the scenario.
        return !productSku.endsWith("3380");
    }
}
