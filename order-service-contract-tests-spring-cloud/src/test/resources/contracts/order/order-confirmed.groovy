package contracts

org.springframework.cloud.contract.spec.Contract.make {
    name "OrderConfirmed Event"
    label 'order'
    
    input {
        triggeredBy('createOrderTriggered()')
    }
    
    outputMessage {
        sentTo 'orders'
        
        body([
            orderId: $(regex('[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}')),
            paymentId: $(regex('[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}')),
            amount: 102.32,
            street: '1203 Westmisnter Blvrd',
            city: 'Westminster',
            state: 'MI',
            zip: '92239',
            country: 'USA'
        ])
        headers {
            header('Content-Type', 'application/json')
        }
    }
}