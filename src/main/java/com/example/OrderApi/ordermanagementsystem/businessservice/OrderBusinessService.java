package com.example.OrderApi.ordermanagementsystem.businessservice;

import com.example.OrderApi.OrderApiPeripheryService;
import com.example.OrderApi.ordermanagementsystem.entities.Order;
import com.example.OrderApi.ordermanagementsystem.entities.OrderItem;
import com.example.OrderApi.ordermanagementsystem.entities.OrderItemStatus;
import com.example.OrderApi.ordermanagementsystem.entities.OrderStatus;
import com.example.OrderApi.ordermanagementsystem.repositories.OrderJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.OrderApi.ordermanagementsystem.businessservice.OrderUtility.*;

@Service
public class OrderBusinessService {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderApiPeripheryService orderApiPeripheryService;

    public OrderBusinessService(final OrderJpaRepository orderJpaRepository, final OrderApiPeripheryService orderApiPeripheryService) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderApiPeripheryService = orderApiPeripheryService;
    }

    public List<Order> findAllOrders() {
        return orderJpaRepository.findAll();
    }

    public String saveOrder(final Order order) {
        final List<OrderItem> orderItems = order.getOrderItems();
        final boolean availabilityCheckResult = orderItems.stream().map(OrderItem::getProductSku).allMatch(orderApiPeripheryService::productInventoryCheck);//availabilityCheck
        if (availabilityCheckResult) {
            enrichOrderItems(orderItems);
            enrichOrder(order);
            orderJpaRepository.save(order);
            return String.format("Order: %s has been created successfully.", order.getOrderNumber());
        }
        return "Order is not created because the availability check was failed for one of the Order Item, please submit the order after sometime.";
    }

    private void enrichOrder(final Order order) {
        order.setTotalAmount(calculateTotalPrice(order));
        order.setOrderNumber(generateOrderNumber(order.getCustomerId()));
        order.setOrderStatus(OrderStatus.PENDING); //persistOrder
    }

    private void enrichOrderItems(final List<OrderItem> orderItems) {
        orderItems.forEach(orderItem -> orderItem.setFinalPrice(getFinalPrice(orderItem)));
        orderItems.forEach(orderItem -> orderItem.setGrossPrice(getFinalPrice(orderItem) * orderItem.getQuantity()));
        orderItems.forEach(orderItem -> orderItem.setDeliveryDate(orderApiPeripheryService.getEstimatedDeliveryDate(orderItem)));//retrieveDeliveryDate
        orderItems.forEach(orderItem -> orderItem.setStatus(OrderItemStatus.PENDING));
    }
}
