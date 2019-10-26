package com.example.order;

import java.util.Map;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit.MessagePactProviderRule;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.messaging.MessagePact;

public class OrderConfirmedConsumerTest {
    private static final String PROVIDER_ID = "Order Service";
    private static final String CONSUMER_ID = "Shipment Service";
    
    @Rule
    public MessagePactProviderRule provider = new MessagePactProviderRule(this);
    private byte[] message;

    @Pact(provider = PROVIDER_ID, consumer = CONSUMER_ID)
    public MessagePact pact(MessagePactBuilder builder) {
        return builder
            .given("default")
            .expectsToReceive("an Order confirmation message")
            .withMetadata(Map.of("Content-Type", "application/json"))
            .withContent(new PactDslJsonBody()
                .uuid("orderId")
                .uuid("paymentId")
                .decimalType("amount")
                .stringType("street")
                .stringType("city")
                .stringType("state")
                .stringType("zip")
                .stringType("country"))
            .toPact();
    }

    @Test
    @PactVerification(PROVIDER_ID)
    public void test() throws Exception {
        Assert.assertNotNull(message);
    }

    public void setMessage(byte[] messageContents) {
        message = messageContents;
    }
}
