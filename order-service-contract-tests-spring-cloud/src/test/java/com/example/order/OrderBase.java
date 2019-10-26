package com.example.order;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMessageVerifier
public class OrderBase {
    @Autowired private MessageChannel orders;
    
    public void createOrderTriggered() {
        OrderConfirmed order = new OrderConfirmed();
        order.setOrderId(UUID.randomUUID());
        order.setPaymentId(UUID.randomUUID());
        order.setAmount(new BigDecimal("102.32"));
        order.setStreet("1203 Westmisnter Blvrd");
        order.setCity("Westminster");
        order.setCountry("USA");
        order.setState("MI");
        order.setZip("92239");

        orders.send(MessageBuilder
            .withPayload(order)
            .setHeader("Content-Type", "application/json")
            .build());
    }
}
