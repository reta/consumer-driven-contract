package com.example.payment;

import org.junit.Before;

import com.example.payment.PaymentController;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

public class PaymentBase {
    @Before
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(new PaymentController());
    }
}
