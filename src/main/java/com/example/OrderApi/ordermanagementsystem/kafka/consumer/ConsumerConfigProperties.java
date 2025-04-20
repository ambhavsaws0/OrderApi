package com.example.OrderApi.ordermanagementsystem.kafka.consumer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "orderSystem.payment.consumer")
public record ConsumerConfigProperties(
        @NotEmpty
        String topics,
        @NotBlank
        String groupId,
        @NotNull
        Integer concurrency,
        @NotBlank
        String containerGroup
        ) {
}
