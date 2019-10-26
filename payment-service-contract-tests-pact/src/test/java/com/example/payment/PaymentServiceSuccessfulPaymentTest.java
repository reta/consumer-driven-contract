package com.example.payment;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;

public class PaymentServiceSuccessfulPaymentTest {
    public static final String PROVIDER_ID = "Payment Service";
    public static final String CONSUMER_ID = "Order Service";
    
    @Rule
    public PactProviderRuleMk2 provider = new PactProviderRuleMk2(PROVIDER_ID, this);
    private PaymentServiceClient client;
    
    @Before
    public void setUp() {
        client = new PaymentServiceClient(provider.getUrl());
    }

    @Pact(provider = PROVIDER_ID, consumer = CONSUMER_ID)
    public RequestResponsePact pact(PactDslWithProvider builder) {
        return builder
            .given("default")
            .uponReceiving("POST new payment")
            .method("POST")
            .path("/payments")
            .headers("Content-Type", "application/json")
            .body(
                new PactDslJsonBody()
                    .uuid("orderId")
                    .decimalType("amount")
                    .timestamp("timestamp", "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                    .object("creditCard")
                        .stringType("number")
                        .stringType("holder")
                        .stringMatcher("expiration", "\\d{2}/\\d{2}", "10/22")
                        .stringMatcher("ccv", "\\d{3}", "111")
                        .closeObject()
            )
            .willRespondWith()
            .status(201)
            .matchHeader("Content-Type", "application/json")
            .body(
                new PactDslJsonBody()
                    .uuid("id", "e2d548c5-e1bf-407f-aed4-c973dc753e3e")
                    .stringType("status", "ACCEPTED")
            )
            .toPact();
    }
    
    @Test
    @PactVerification(PROVIDER_ID)
    public void test() throws IOException, InterruptedException {
        final Map<?, ?> expected = Map.of(
            "id", "e2d548c5-e1bf-407f-aed4-c973dc753e3e", 
            "status", "ACCEPTED");
        
        assertEquals(client.pay(new BigDecimal("102.32")), expected);
    }
}
