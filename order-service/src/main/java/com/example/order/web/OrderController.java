package com.example.order.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    path = "/orders", 
    consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE
)
public class OrderController {
	@PostMapping
	public ResponseEntity<OrderStatus> order(@RequestBody OrderRequest order) {
		return new ResponseEntity<>(new OrderStatus(order.getId(), Status.ACCEPTED),
		    HttpStatus.CREATED); 
	}
}
