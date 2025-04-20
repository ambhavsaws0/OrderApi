package com.example.OrderApi.ordermanagementsystem.restcontroller;

import com.example.OrderApi.ordermanagementsystem.businessservice.OrderBusinessService;
import com.example.OrderApi.ordermanagementsystem.businessservice.UpdateOrderService;
import com.example.OrderApi.ordermanagementsystem.entities.Order;
import com.example.OrderApi.ordermanagementsystem.validation.OrderDataValidator;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderApiRestController {

    private final OrderDataValidator orderDataValidator;
    private final OrderBusinessService orderBusinessService;
    private final UpdateOrderService updateOrderService;

    public OrderApiRestController(final OrderDataValidator orderDataValidator, final OrderBusinessService orderBusinessService, final UpdateOrderService updateOrderService) {
        this.orderDataValidator = orderDataValidator;
        this.orderBusinessService = orderBusinessService;
        this.updateOrderService = updateOrderService;
    }

    @PostMapping("/createOrder")
    public String createOrder(@RequestBody final Order order) {
        final List<String> validationErrors = orderDataValidator.validateOrder(order);
        if (validationErrors.isEmpty()) {
            return orderBusinessService.saveOrder(order);
        }
        return "Order is not created, validations failed: " + String.join("", validationErrors);
    }

    @GetMapping("/findAllOrders")
    public Iterable<Order> findAllOrders() {
        return orderBusinessService.findAllOrders();
    }

    @PutMapping("/updatePaymentStatus")
    public String updateOrder(@RequestBody final Order order) {
        return updateOrderService.updateOrderStatus(order);
    }
}
