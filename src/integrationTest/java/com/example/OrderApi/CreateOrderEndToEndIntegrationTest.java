package com.example.OrderApi;

import com.example.OrderApi.ordermanagementsystem.dto.OrderItemResponseDto;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@EmbeddedKafka(brokerProperties = {"auto.create.topics.enable=true"})
public class CreateOrderEndToEndIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Test
    public void test_createOrder_Success() throws Exception {
        //send HTTP Request and retrieve response
        final MvcResult mvcResult = mockMvc.perform(
                post("/orderApi/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRequest()))).andExpect(status().isOk()).andReturn();

        final OrderResponseDto resultOrderResponseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderResponseDto.class);
        verifyOrder(resultOrderResponseDto);
        final List<OrderItemResponseDto> resultOrderItemResponseList = resultOrderResponseDto.orderItems().stream().toList();
        assertEquals(1, resultOrderItemResponseList.size());
        final OrderItemResponseDto resultOrderItemResponse = resultOrderResponseDto.orderItems().stream().toList().getFirst();
        verifyOrderItems(resultOrderItemResponse);
        //verify from database
        verifyOrderFromDB();
    }

    private void verifyOrderFromDB() {
        final List<Order> allOrder = orderJpaRepository.findAll();
        final Order orderEntity = allOrder.getFirst();
        assertEquals(1, allOrder.size());
        assertEquals(42L, orderEntity.getCustomerId());
        assertEquals(OrderStatus.PENDING, orderEntity.getOrderStatus());
        assertTrue(orderEntity.getOrderNumber().contains(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)));
    }

    private static void verifyOrderItems(OrderItemResponseDto resultOrderItemResponse) {
        assertEquals("Second Product", resultOrderItemResponse.productName());
        assertEquals(1, resultOrderItemResponse.quantity());
        assertEquals(OrderItemStatus.PENDING, resultOrderItemResponse.orderItemStatus());
    }

    private static void verifyOrder(OrderResponseDto resultOrderResponseDto) {
        assertEquals(42L, resultOrderResponseDto.customerNumber());
        assertEquals(OrderStatus.PENDING, resultOrderResponseDto.orderStatus());
    }

    @Test
    public void test_createOrder_AvailabilityCheckFailed() throws Exception {
        final Order inputOrder = buildRequest();
        inputOrder.getOrderItems().forEach(item -> item.setProductSku("f9c3bc0c-371a-4420-ab6f-efed11723380"));//Availability check failed for this ProductSku
        //send HTTP Request and retrieve response
        final MvcResult mvcResult = mockMvc.perform(
                post("/orderApi/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputOrder))).andExpect(status().is4xxClientError()).andReturn();
        final ProblemDetail problemDetail = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ProblemDetail.class);
        assertEquals("Availability Check Failed", problemDetail.getTitle());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), problemDetail.getStatus());
        assertEquals("Order is not created because the availability check was failed for one of the Order Item, please submit the order after sometime.", Objects.requireNonNull(problemDetail.getProperties()).get("message"));
    }

    @Test
    public void test_createOrder_OrderValidationFailed() throws Exception {
        final Order inputOrder = buildRequest();
        inputOrder.getOrderItems().forEach(item -> item.setQuantity(0));
        //send HTTP Request and retrieve response
        final MvcResult mvcResult = mockMvc.perform((post("/orderApi/orders")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(inputOrder)))).andExpect(status().is4xxClientError()).andReturn();
        final ProblemDetail problemDetail = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ProblemDetail.class);
        assertEquals("Order validation failed", problemDetail.getTitle());
        assertEquals(HttpStatus.BAD_REQUEST.value(), problemDetail.getStatus());
        assertEquals("total quantity of order items is 0", Objects.requireNonNull(problemDetail.getProperties()).get("message"));
    }

    private static Order buildRequest() {
        final OrderItem orderItem = OrderItem.builder()
                .productId(1L)
                .productName("Second Product")
                .productSku("f9c3bc0c-371a-4420-ab6f-efed11723383")
                .quantity(1)
                .unitPrice(1.2d)
                .discountPercent(10.0d)
                .taxPercent(20.0d)
                .shippingDetail(ShippingDetail.builder().weight(12).courier("Next Door").dimensions("10*29*32").build())
                .build();
        final Order order = new Order();
        order.setCustomerId(42L);
        order.setOrderItems(List.of(orderItem));
        return order;
    }
}
