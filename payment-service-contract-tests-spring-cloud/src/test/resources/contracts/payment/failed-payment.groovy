package contracts

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'POST'
        url '/payments'
        body([
               orderId: $(regex('[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}')),
               amount : 0,
               timestamp: '2019-09-29T20:43:03.6977944-04:00',
               creditCard: [
                   number: $(regex('[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{4}')),
                   holder: 'John Smith',
                   expiration: $(regex('[0-9]{2}/[0-9]{2}')),
                   ccv: $(regex('[0-9]{3}'))
              ]
        ])
        headers {
            contentType('application/json')
        }
    }
    response {
        status 400
        body([
            instance: 'error',
            type: 'http://error',
            detail: 'error detail' 
        ])
        headers {
            contentType('application/json')
        }
    }
}