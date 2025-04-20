package com.example.OrderApi.deliverysystem;

import com.example.OrderApi.ordermanagementsystem.entities.OrderItem;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Random;

@Component
public class EstimatedDeliveryDateProviderService {

    public LocalDate retrieveEstimatedDeliveryDate(final OrderItem orderItem) {
        return LocalDate.now().plusDays(new Random().nextInt(10));
    }
}
