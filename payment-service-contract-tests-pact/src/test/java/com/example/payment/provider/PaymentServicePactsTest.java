package com.example.payment.provider;

import static com.github.restdriver.clientdriver.RestClientDriver.giveResponse;
import static com.github.restdriver.clientdriver.RestClientDriver.onRequestTo;

import org.junit.ClassRule;
import org.junit.runner.RunWith;

import com.github.restdriver.clientdriver.ClientDriverRequest.Method;
import com.github.restdriver.clientdriver.ClientDriverRule;

import au.com.dius.pact.provider.junit.PactRunner;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;

@RunWith(PactRunner.class)
@Provider(PaymentServicePactsTest.PROVIDER_ID)
@PactFolder("pacts")
public class PaymentServicePactsTest {
    public static final String PROVIDER_ID = "Payment Service";
    public static final String CONSUMER_ID = "Order Service";
    
    @ClassRule
    public static final ClientDriverRule embeddedProvider = new ClientDriverRule(0);

    @TestTarget
    public final Target target = new HttpTarget(embeddedProvider.getPort());
    
    @State("default")
    public void toDefaultState() {
        embeddedProvider.addExpectation(
            onRequestTo("/payments").withMethod(Method.POST), 
                giveResponse("{\"id\": \"b5d548c5-e1bf-407f-aed4-c973dc753e3b\", \"status\": \"ACCEPTED\"}", 
                    "application/json").withStatus(201)
          );
    }
    
    @State("failure")
    public void toFailureState() {
        embeddedProvider.addExpectation(
            onRequestTo("/payments").withMethod(Method.POST), 
                giveResponse("{\"type\": \"http://error\", \"detail\": \"error detail\", \"instance\": \"error\"}", 
                    "application/json").withStatus(400)
          );
    }
    

    public ClientDriverRule embeddedProvider() {
      return embeddedProvider;
    }
}
