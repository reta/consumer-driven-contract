package com.example.payment;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PaymentServiceClient {
    private final HttpClient client;  
    private final String uri;
    private final ObjectMapper mapper;
    
    public PaymentServiceClient(String uri) {
        this.client = HttpClient.newHttpClient();
        this.uri = uri;
        this.mapper = new ObjectMapper();
    }

    public Map<?, ?> pay(BigDecimal amount) throws IOException, InterruptedException {
        final Map<?, ?> data = Map
            .ofEntries(
                Map.entry("orderId", "e2d548c5-e1bf-407f-aed4-c973dc753e3e"),
                Map.entry("amount", amount),
                Map.entry("timestamp", "2019-09-29T20:43:03.6977944-04:00"),
                Map.entry("creditCard", Map.ofEntries(
                    Map.entry("number", "2222-1111-2333-2211"),
                    Map.entry("holder", "John Smith"),
                    Map.entry("expiration", "10/22"),
                    Map.entry("ccv", "111")
                ))
            );
        
        HttpRequest request = HttpRequest
              .newBuilder()
              .uri(URI.create(uri + "/payments"))
              .POST(BodyPublishers.ofString(mapper.writeValueAsString(data)))
              .header("Content-Type", "application/json")
              .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        return mapper.readValue(response.body(), Map.class);  
    }

}
