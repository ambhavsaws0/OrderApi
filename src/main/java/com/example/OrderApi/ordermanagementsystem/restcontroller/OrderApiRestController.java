package com.example.OrderApi.ordermanagementsystem.restcontroller;

import com.example.OrderApi.ordermanagementsystem.businessservice.OrderBusinessService;
import com.example.OrderApi.ordermanagementsystem.businessservice.UpdateOrderService;
import com.example.OrderApi.ordermanagementsystem.dto.OrderResponseDto;
import com.example.OrderApi.ordermanagementsystem.entities.Order;
import com.example.OrderApi.ordermanagementsystem.validation.OrderDataValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orderApi")
@Tag(name = "Orders", description = "Order management APIs")
public class OrderApiRestController {

    private final OrderDataValidator orderDataValidator;
    private final OrderBusinessService orderBusinessService;
    private final UpdateOrderService updateOrderService;

    public OrderApiRestController(final OrderDataValidator orderDataValidator, final OrderBusinessService orderBusinessService, final UpdateOrderService updateOrderService) {
        this.orderDataValidator = orderDataValidator;
        this.orderBusinessService = orderBusinessService;
        this.updateOrderService = updateOrderService;
    }

    @Operation(summary = "Create a new order", description = "Creates a new order with customer and item details.")
    @PostMapping("/orders")
    public OrderResponseDto createOrder(@RequestBody final Order order) {
        orderDataValidator.validateOrder(order);
        return orderBusinessService.saveOrder(order);
    }

    @Operation(summary = "Get all orders", description = "Retrieve all orders with corresponding line items.")
    @GetMapping("/orders")
    public Iterable<Order> findAllOrders() {
        return orderBusinessService.findAllOrders();
    }

    @Operation(summary = "Update an order", description = "Update Order Status.")
    @PutMapping("/orders")
    public OrderResponseDto updateOrder(@RequestBody final Order order) {
        return updateOrderService.updateOrderStatus(order);
    }
}
