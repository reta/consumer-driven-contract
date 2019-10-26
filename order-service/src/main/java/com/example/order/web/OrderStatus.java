package com.example.order.web;

import java.util.UUID;

public class OrderStatus {
    private final UUID id;
    private final Status status;
    
    public OrderStatus(UUID id, Status status) {
        this.id = id;
        this.status = status;
    }
    
    public UUID getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }
}
