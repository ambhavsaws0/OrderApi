package com.example.OrderApi;

import com.example.OrderApi.ordermanagementsystem.entities.Order;
import com.example.OrderApi.ordermanagementsystem.entities.OrderStatus;
import com.example.OrderApi.ordermanagementsystem.kafka.event.PaymentEvent;
import com.example.OrderApi.ordermanagementsystem.repositories.OrderJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static com.example.OrderApi.ordermanagementsystem.businessservice.OrderUtility.generateOrderNumber;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@EmbeddedKafka(topics = {"order-payment-topic", "payment-topic"}, partitions = 3)
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}", "spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}"})
public class PaymentUpdateEventKafkaEndToEndIntegrationTest {

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;
    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    KafkaListenerEndpointRegistry endpointRegistry;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OrderJpaRepository orderJpaRepository;

    private Consumer<String, String> kafkaConsumer;

    @BeforeEach
    void setUp() {
        for (MessageListenerContainer messageListenerContainer : endpointRegistry.getListenerContainers()) {
            ContainerTestUtils.waitForAssignment(messageListenerContainer, embeddedKafkaBroker.getPartitionsPerTopic());
        }

        var configs = new HashMap<>(KafkaTestUtils.consumerProps("group1", "true", embeddedKafkaBroker));
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        kafkaConsumer = new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), new StringDeserializer()).createConsumer();
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(kafkaConsumer, "payment-topic");
    }

    @Test
    public void test_OnMessage() throws ExecutionException, InterruptedException, IOException {
        //given
        final Order order = buildOrder();
        orderJpaRepository.save(order);
        final String paymentEvent = String.format("""
                {
                    "orderNumber": "%s",
                    "totalAmount": 23.2
                 }
                """, order.getOrderNumber());
        //send kafka event to the order-payment-topic for PaymentEventConsumer
        kafkaTemplate.send("order-payment-topic", paymentEvent).get();

        //read the kafka event from the 'payment-topic' to verify the PaymentEventConsumer flow
        final ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(kafkaConsumer);
        //verify the 'payment-topic' event
        assertEquals(1, records.count());
        assertEquals(order.getOrderNumber(), records.iterator().next().key());
        assertEquals(new PaymentEvent(order.getOrderNumber(), 23.2), objectMapper.readValue(records.iterator().next().value(), PaymentEvent.class));
    }


    private static Order buildOrder() {
        final Order order = new Order();
        order.setCustomerId(42L);
        order.setOrderNumber(generateOrderNumber(42L));
        order.setOrderStatus(OrderStatus.PENDING);
        return order;
    }
}
