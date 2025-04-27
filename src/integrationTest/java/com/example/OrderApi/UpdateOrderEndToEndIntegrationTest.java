package com.example.OrderApi;

import com.example.OrderApi.ordermanagementsystem.dto.OrderResponseDto;
import com.example.OrderApi.ordermanagementsystem.entities.*;
import com.example.OrderApi.ordermanagementsystem.repositories.OrderJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Objects;

import static com.example.OrderApi.ordermanagementsystem.businessservice.OrderUtility.generateOrderNumber;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@EmbeddedKafka(brokerProperties = {"auto.create.topics.enable=true"})
public class UpdateOrderEndToEndIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Test
    public void test_updateOrder_Success() throws Exception {
        //given
        final Order orderEntity = buildOrder();
        orderJpaRepository.save(orderEntity);
        //send HTTP Request and retrieve the response
        final MvcResult mvcResult = mockMvc.perform(
                put("/orderApi/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRequest(orderEntity.getOrderNumber())))).andExpect(status().isOk()).andReturn();
        final OrderResponseDto resultOrderResponseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderResponseDto.class);
        // verify
        assertSame(OrderStatus.PAID, resultOrderResponseDto.orderStatus());
    }

    @Test
    public void test_updateOrder_InvalidOrderStatus() throws Exception {
        //given
        final Order orderEntity = buildOrder();
        orderJpaRepository.save(orderEntity);

        //send HTTP Request and retrieve the response
        final MvcResult mvcResult = mockMvc.perform(
                put("/orderApi/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRequestStatusShipped(orderEntity.getOrderNumber())))).andExpect(status().is4xxClientError()).andReturn();
        final OrderResponseDto resultOrderResponseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderResponseDto.class);

        //verify
        assertNotSame(OrderStatus.SHIPPED, resultOrderResponseDto.orderStatus());
        final ProblemDetail problemDetail = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ProblemDetail.class);
        assertEquals("Invalid Order Status", problemDetail.getTitle());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), problemDetail.getStatus());
        assertEquals(String.format("As the current status of the Order: %s is %s, it is not allowed to set the status to %s.",
                orderEntity.getOrderNumber(), OrderStatus.PENDING, OrderStatus.SHIPPED), Objects.requireNonNull(problemDetail.getProperties()).get("message"));
    }

    private static Order buildRequest(final String orderNumber) {
        final Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setOrderStatus(OrderStatus.PAID);
        return order;
    }

    private static Order buildRequestStatusShipped(final String orderNumber) {
        final Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setOrderStatus(OrderStatus.SHIPPED);
        return order;
    }

    private static Order buildOrder() {
        final Order order = new Order();
        order.setCustomerId(42L);
        order.setOrderNumber(generateOrderNumber(42L));
        order.setOrderStatus(OrderStatus.PENDING);
        return order;
    }
}
