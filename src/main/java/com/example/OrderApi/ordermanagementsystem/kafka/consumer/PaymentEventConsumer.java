package com.example.OrderApi.ordermanagementsystem.kafka.consumer;

import com.example.OrderApi.ordermanagementsystem.kafka.event.PaymentEvent;
import com.example.OrderApi.ordermanagementsystem.kafka.producer.PaymentEventProducer;
import com.example.OrderApi.ordermanagementsystem.repositories.OrderJpaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentEventConsumer {
    final ObjectMapper objectMapper;
    final PaymentEventProducer paymentEventProducer;
    final OrderJpaRepository orderJpaRepository;

    public PaymentEventConsumer(final ObjectMapper objectMapper, final PaymentEventProducer paymentEventProducer, final OrderJpaRepository orderJpaRepository) {
        this.objectMapper = objectMapper;
        this.paymentEventProducer = paymentEventProducer;
        this.orderJpaRepository = orderJpaRepository;
    }

    @KafkaListener(topics = "#{'${orderSystem.payment.consumer.topics}'.split(',')}", groupId = "${orderSystem.payment.consumer.groupId}")
    public void onMessage(ConsumerRecord<Long, String> consumerRecord) {
        final String value = consumerRecord.value();
        try {
            final PaymentEvent paymentEvent = objectMapper.readValue(value, PaymentEvent.class);
            final String orderNumber = paymentEvent.orderNumber();
            log.info("Received payment update on OM System for Order Number: {}", orderNumber);
            orderJpaRepository.findByOrderNumber(orderNumber)
                    .ifPresentOrElse(order -> {
                        try {
                            paymentEventProducer.sendPaymentEvent(paymentEvent);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }, () -> log.info("Order Number {} in the event is not valid.", orderNumber));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
