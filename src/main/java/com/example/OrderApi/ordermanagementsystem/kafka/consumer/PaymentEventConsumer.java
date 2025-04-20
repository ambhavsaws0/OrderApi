package com.example.OrderApi.ordermanagementsystem.kafka.consumer;

import com.example.OrderApi.ordermanagementsystem.kafka.event.PaymentEvent;
import com.example.OrderApi.ordermanagementsystem.kafka.producer.PaymentEventProducer;
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

    public PaymentEventConsumer(final ObjectMapper objectMapper, final PaymentEventProducer paymentEventProducer) {
        this.objectMapper = objectMapper;
        this.paymentEventProducer = paymentEventProducer;
    }

    @KafkaListener(topics = "#{'${orderSystem.payment.consumer.topics}'.split(',')}", groupId = "${orderSystem.payment.consumer.groupId}")
    public void onMessage(ConsumerRecord<Long, String> consumerRecord) {
        final String value = consumerRecord.value();
        try {
            final PaymentEvent paymentEvent = objectMapper.readValue(value, PaymentEvent.class);
            log.info("Received payment update on OM System for Order Number: {}", paymentEvent.orderNumber());
            paymentEventProducer.sendPaymentEvent(paymentEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
