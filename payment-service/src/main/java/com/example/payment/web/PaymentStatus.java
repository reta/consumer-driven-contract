package com.example.payment.web;

import java.util.UUID;

import javax.validation.constraints.NotNull;

public class PaymentStatus {
	@NotNull private UUID id;
	@NotNull private Status status;
	private String reason;
	
	public static enum Status {
	    ACCEPTED,
	    REJECTED
	}

	public PaymentStatus() {
	}
		
	public UUID getId() {
		return id;
	}
	
	public void setId(UUID id) {
		this.id = id;
	}

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}