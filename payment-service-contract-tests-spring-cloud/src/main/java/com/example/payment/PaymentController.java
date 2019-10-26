package com.example.payment;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
    @PostMapping(path = "/payments") 
    ResponseEntity<?> payment(@RequestBody Map<?, ?> request) {
        if (request.get("amount").equals(Integer.valueOf(0))) {
            return ResponseEntity
                .status(400)
                .body(Map.of("instance", "error", "type", "http://error", "detail", "error detail"));
        }
        
        return ResponseEntity
            .status(201)
            .body(Map.of("id", UUID.randomUUID(), "status", "ACCEPTED")); 
    }
}

