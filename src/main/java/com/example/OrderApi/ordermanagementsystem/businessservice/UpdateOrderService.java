package com.example.OrderApi.ordermanagementsystem.businessservice;

import com.example.OrderApi.ordermanagementsystem.dto.OrderResponseDto;
import com.example.OrderApi.ordermanagementsystem.entities.Order;
import com.example.OrderApi.ordermanagementsystem.entities.OrderStatus;
import com.example.OrderApi.ordermanagementsystem.exception.BusinessException;
import com.example.OrderApi.ordermanagementsystem.repositories.OrderJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

import static com.example.OrderApi.ordermanagementsystem.entities.OrderStatus.nextPossibleOrderStatus;

@Component
public class UpdateOrderService {

    final OrderJpaRepository orderJpaRepository;

    public UpdateOrderService(final OrderJpaRepository orderJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
    }

    public OrderResponseDto updateOrderStatus(final Order order) {
        final String orderNumber = order.getOrderNumber();
        final Optional<Order> optionalOrder = orderJpaRepository.findByOrderNumber(orderNumber);

        if (optionalOrder.isPresent()) {
            final OrderStatus nextOrderStatus = order.getOrderStatus();
            final Order currentOrder = optionalOrder.get();
            final OrderStatus currentOrderStatus = currentOrder.getOrderStatus();

            if (nextPossibleOrderStatus(currentOrderStatus).contains(nextOrderStatus)) {
                currentOrder.setOrderStatus(nextOrderStatus);
                orderJpaRepository.save(currentOrder);
                return new OrderResponseDto(orderNumber, order.getCustomerId(), nextOrderStatus, Collections.emptyList());
            } else {
                throw new BusinessException("Invalid Order Status", String.format("As the current status of the Order: %s is %s, it is not allowed to set the status to %s.", orderNumber, currentOrderStatus, nextOrderStatus));
            }
        }
        throw new BusinessException("Invalid Order Error", String.format("Order: %s does not exist in the system.", order.getOrderNumber()));
    }
}
