package com.example.order;

import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.order.config.ServiceConfiguration;

import io.github.robwin.swagger.test.SwaggerAssertions;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceConfiguration.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class OrderServiceTest {
    @LocalServerPort
    private int port;

    @Test 
    public void validate() throws URISyntaxException { 
        SwaggerAssertions
            .assertThat("http://localhost:" + port + "/services/v2/api-docs")
            .isEqualTo(getClass().getResource("/contracts/published.json").toExternalForm()); 
    }

}
