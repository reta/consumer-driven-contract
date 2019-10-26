package com.example.payment;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import javax.json.Json;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.util.SocketUtils;

import com.atlassian.oai.validator.pact.ValidatedPactProviderRule;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;

public class PaymentServiceContractTest {
    private static final String PROVIDER_ID = "Payment Service";
    private static final String CONSUMER_ID = "Order Service";
    
    private ValidatedPactProviderRule provider;
    
    @Rule
    public ValidatedPactProviderRule getValidatedPactProviderRule() throws IOException {
        if (provider == null) {
            provider = new ValidatedPactProviderRule(IOUtils.toString(getClass().getResource("/contract/openapi.json"), StandardCharsets.UTF_8), null, PROVIDER_ID, 
                 "localhost", SocketUtils.findAvailableTcpPort(), this);
        }
        return provider;
    }
    
    @Pact(provider = PROVIDER_ID, consumer = CONSUMER_ID)
    public RequestResponsePact processPayment(PactDslWithProvider builder) {
        return builder
            .uponReceiving("POST new payment")
            .method("POST")
            .path("/payments")
            .headers(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .body(
                new PactDslJsonBody()
                    .uuid("orderId")
                    .decimalType("amount", new BigDecimal(102.33d))
                    .stringType("notes")
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
            .matchHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .body(
                new PactDslJsonBody()
                    .uuid("id")
                    .stringMatcher("status", "REJECTED|ACCEPTED", "ACCEPTED")
            )
            .toPact();
    }

    @Pact(provider = PROVIDER_ID, consumer = CONSUMER_ID)
    public RequestResponsePact failedPayment(PactDslWithProvider builder) {
        return builder
            .uponReceiving("POST new payment")
            .method("POST")
            .path("/payments")
            .headers(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .body(
                new PactDslJsonBody()
                    .uuid("orderId")
                    .decimalType("amount", new BigDecimal(102.33d))
                    .timestamp("timestamp", "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                    .object("creditCard")
                        .stringType("number")
                        .stringType("holder")
                        .stringMatcher("expiration", "\\d{2}/\\d{2}", "10/22")
                        .stringMatcher("ccv", "\\d{3}", "111")
                        .closeObject()
            )
            .willRespondWith()
            .status(400)
            .matchHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .body(
                new PactDslJsonBody()
                    .stringType("instance")
                    .stringType("type")
                    .stringType("detail")
            )
            .toPact();
    }

    @Test
    @PactVerification(value = PROVIDER_ID, fragment = "processPayment")
    public void testProcessPayment() {
        given()
            .baseUri(provider.getConfig().url())
            .contentType(ContentType.JSON)
            .body(Json
                .createObjectBuilder()
                .add("orderId", "e2d548c5-e1bf-407f-aed4-c973dc753e3e")
                .add("amount", new BigDecimal(102.33d))
                .add("timestamp", OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .add("notes","Purchase Order #1")
                .add("creditCard", Json
                    .createObjectBuilder()
                    .add("number", "2222-1111-2333-2211")
                    .add("holder", "John Smith")
                    .add("expiration", "10/22")
                    .add("ccv", "111"))
                .build(), ObjectMapperType.JOHNZON)
            .post("/payments")
            .then()
            .log()
            .all();
    }
    
    @Test
    @PactVerification(value = PROVIDER_ID, fragment = "failedPayment")
    public void testFailedPayment() {
        given()
            .baseUri(provider.getConfig().url())
            .contentType(ContentType.JSON)
            .body(Json
                .createObjectBuilder()
                .add("orderId", "e2d548c5-e1bf-407f-aed4-c973dc753e3e")
                .add("amount", new BigDecimal(102.33d))
                .add("timestamp", OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .add("creditCard", Json
                    .createObjectBuilder()
                    .add("number", "2222-1111-2333-2211")
                    .add("holder", "John Smith")
                    .add("expiration", "10/22")
                    .add("ccv", "111"))
                .build(), ObjectMapperType.JOHNZON)
            .post("/payments")
            .then()
            .log()
            .all();
    }

}
