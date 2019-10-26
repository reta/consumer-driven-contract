package com.example.order.provider;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.Before;
import org.junit.runner.RunWith;

import com.example.order.OrderConfirmed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit.PactRunner;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.AmqpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
//import groovy.json.JsonOutput;

@RunWith(PactRunner.class)
@Provider(OrderServicePactsTest.PROVIDER_ID)
@PactFolder("pacts") 
public class OrderServicePactsTest {
    public static final String PROVIDER_ID = "Order Service";
    public static final String CONSUMER_ID = "Shipment Service";

    @TestTarget
    public final Target target = new AmqpTarget();
    private ObjectMapper objectMapper;
    
    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @State("default")
    public void toDefaultState() {
    }
    
    @PactVerifyProvider("an Order confirmation message")
    public String verifyOrderConfirmed() throws JsonProcessingException {
        final OrderConfirmed order = new OrderConfirmed();
        
        order.setOrderId(UUID.randomUUID());
        order.setPaymentId(UUID.randomUUID());
        order.setAmount(new BigDecimal("102.33"));
        order.setStreet("1203 Westmisnter Blvrd");
        order.setCity("Westminster");
        order.setCountry("USA");
        order.setState("MI");
        order.setZip("92239");

        return objectMapper.writeValueAsString(order);
    }

}