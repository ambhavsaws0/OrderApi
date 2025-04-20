package com.example.OrderApi.ordermanagementsystem.kafka.producer;

import com.example.OrderApi.ordermanagementsystem.kafka.event.PaymentEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventProducer {

    @Value("${paymentSystem.kafka.producer.topic}")
    private String topicName;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentEventProducer.class);

    public PaymentEventProducer(final KafkaTemplate<String, String> kafkaTemplate, final ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendPaymentEvent(final PaymentEvent paymentEvent) throws JsonProcessingException {
        LOGGER.info("topicName: {}, paymentEvent:  {}", topicName, objectMapper.writeValueAsString(paymentEvent));
        kafkaTemplate.send(topicName, paymentEvent.orderNumber(), objectMapper.writeValueAsString(paymentEvent));
    }
}
