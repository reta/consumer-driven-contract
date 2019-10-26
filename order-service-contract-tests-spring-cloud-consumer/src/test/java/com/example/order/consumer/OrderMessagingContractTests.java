package com.example.order.consumer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubFinder;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.messaging.Message;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMessageVerifier
@AutoConfigureStubRunner(ids = "com.example:order-service-contract-tests-spring-cloud:+:stubs", stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class OrderMessagingContractTests {
    @Autowired private ObjectMapper objectMapper;
    @Autowired private MessageVerifier<Message<?>> verifier;
    @Autowired private StubFinder stubFinder;
    
    @Test
    public void testOrderConfirmed() throws Exception {
        stubFinder.trigger("order");
        
        final Message<?> message = verifier.receive("orders");
        assertThat(message)
            .isNotNull()
            .extracting(Message::getPayload)
            .isNotNull();
        

        final Map<String, Object> payload = objectMapper
            .readValue(message.getPayload().toString(), 
                new TypeReference<Map<String, Object>>() {});
        
        assertThat(payload)
            .containsKeys("orderId", "paymentId")
            .containsEntry("amount", 102.32)
            .containsEntry("street", "1203 Westmisnter Blvrd")
            .containsEntry("city", "Westminster")
            .containsEntry("state", "MI")
            .containsEntry("zip", "92239")
            .containsEntry("country", "USA");
    }
}