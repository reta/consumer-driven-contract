package com.example.payment.web;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;
import org.zalando.problem.Problem;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "payment")
@Component
@Path("/payments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PaymentResource {
	@POST
	@Operation(description = "Process payment", responses = 
        {
    		@ApiResponse(responseCode = "201", description = "Payment processed successfully", 
    		    content = @Content(schema = @Schema(implementation = PaymentStatus.class))),
            @ApiResponse(responseCode = "400", description = "Payment verification failed", 
                content = @Content(schema = @Schema(implementation = Problem.class)))
    	}
	)
	public Response process(final PaymentRequest payment) {
		return Response.ok(new PaymentStatus()).build();
	}
}
