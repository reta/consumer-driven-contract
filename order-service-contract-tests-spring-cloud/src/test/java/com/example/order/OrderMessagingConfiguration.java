package com.example.order;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.PollableChannel;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootConfiguration
public class OrderMessagingConfiguration {
    @Bean
    PollableChannel orders() {
        return MessageChannels.queue().get();
    }
    
    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    PollerMetadata poller() {
        return Pollers.fixedRate(100).get();
    }
    
    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
