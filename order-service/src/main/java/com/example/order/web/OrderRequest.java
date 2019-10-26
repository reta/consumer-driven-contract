package com.example.order.web;

import java.math.BigInteger;
import java.util.UUID;

import io.swagger.annotations.ApiModelProperty;

public class OrderRequest {
    @ApiModelProperty(required = false) private UUID id;
    private BigInteger amount;
    
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }
}
