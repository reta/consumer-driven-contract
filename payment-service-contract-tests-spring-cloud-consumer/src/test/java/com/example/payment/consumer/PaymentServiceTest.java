package com.example.payment.consumer;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerPort;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.ibm.icu.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@AutoConfigureStubRunner(ids = "com.example:payment-service-contract-tests-spring-cloud:+:stubs", stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class PaymentServiceTest {
    @StubRunnerPort("payment-service-contract-tests-spring-cloud")
    private int port;
    private RestTemplate template;
    
    @Before
    public void setUp() {
        template = new RestTemplate();
        template.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
            }
        });
    }
    
    @Test
    public void testSuccessfulPayment() {
        final Map<String, ?> payment = Map
            .ofEntries(
                Map.entry("orderId", "e2d548c5-e1bf-407f-aed4-c973dc753e3e"),
                Map.entry("amount", new BigDecimal("102.32")),
                Map.entry("timestamp", "2019-09-29T20:43:03.6977944-04:00"),
                Map.entry("creditCard", Map.ofEntries(
                    Map.entry("number", "2222-1111-2333-2211"),
                    Map.entry("holder", "John Smith"),
                    Map.entry("expiration", "10/22"),
                    Map.entry("ccv", "111")
                ))
            );

        final ResponseEntity<Map<String, Object>> response = payment(payment);
        assertThat(response)
            .satisfies(r -> {
                assertThat(r.getStatusCode()).isEqualTo(HttpStatus.CREATED);
                assertThat(r.getBody())
                    .containsKey("id")
                    .containsEntry("status", "ACCEPTED");
            });
    }
    
    
    @Test
    public void testFailingPayment() {
        final Map<String, ?> payment = Map
            .ofEntries(
                Map.entry("orderId", "e2d548c5-e1bf-407f-aed4-c973dc753e3e"),
                Map.entry("amount", BigDecimal.ZERO),
                Map.entry("timestamp", "2019-09-29T20:43:03.6977944-04:00"),
                Map.entry("creditCard", Map.ofEntries(
                    Map.entry("number", "2222-1111-2333-2211"),
                    Map.entry("holder", "John Smith"),
                    Map.entry("expiration", "10/22"),
                    Map.entry("ccv", "111")
                ))
            );

        final ResponseEntity<Map<String, Object>> response = payment(payment);
        assertThat(response)
            .satisfies(r -> {
                assertThat(r.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                assertThat(r.getBody())
                    .containsEntry("instance", "error")
                    .containsEntry("type", "http://error")
                    .containsEntry("detail", "error detail");
            });
    }

    private ResponseEntity<Map<String, Object>> payment(final Map<?, ?> payment) {
        final ParameterizedTypeReference<Map<String, Object>> type = new ParameterizedTypeReference<Map<String, Object>>() {};
        return template.exchange("http://localhost:" + port + "/payments", HttpMethod.POST, new HttpEntity<>(payment), type);
    }
}
