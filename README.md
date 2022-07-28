## Implementation of Circuit-breaker pattern ##

### Bootstrap ###
To bootstrap the app you need to build jars (open terminal in the project root directory):

    mvn clean install

Then you will be able to run those jars:

    java -jar ./order-service/target/order-service-0.0.1-SNAPSHOT.jar
    java -jar ./product-service/target/product-service-0.0.1-SNAPSHOT.jar

### Implementation details ###

This implementation consists of two services:
* order-service
* product-service

#### Order service ####
This service has only one function - to fetch orders from DB (H2 is used).
Example of order:

    {
        "originalOrderId": "111-12345-111",
        "products": [
            {
                "productId": 1,
                "price": 100.50,
                "stockQuantity": 10
            }
        ],
        "price": 100.50
    }

To fetch orders you have to execute a `GET` request:

    curl --location --request GET 'http://localhost:5050/orders'

As you can see - this order contains product objects. These products are fetched from the
`product-service`. `ProductConnector` class is used to fetch data from the `product-service`.
The highest level of abstraction is `ProductConnector` interface. `DefaultProductConnector` and `ProductConnectorProxy`
implements this interface.

`ProductConnectorProxy` has special object inside: `CircuitBreakerStrategy` - main interface,
that is implemented by all the circuit-breaker strategies to represent a possible state of `circuit-breaker`:
* Closed (`ClosedCircuitBreakerStrategy`)
* Half-Opened (`HalfOpenedCircuitBreakerStrategy`)
* Opened (`OpenedCircuitBreakerStrategy`)

`ProductConnectorProxy` delegates a call to the corresponding strategy, then it checks if the strategy should be changed.
Each strategy has its implementation to decide if it should be changed, and what strategy should be the next.
If the strategy is changed - its statistics (count of requests, count of failed requests, etc.) get reset.
This approach was used to reduce the complexity of concurrent execution.
The other possible approach is to have a separate demon thread that will periodically reset the statistics if necessary.
This means that the statistics will be reset only after the next call to the `order-service`.
To see the circuit-breaker details you can execute the `GET` request:

    curl --location --request GET 'http://localhost:5050/circuit-breaker/details'

Example of the response:

    Strategy: ClosedCircuitBreakerStrategy
    count of recorded requests: 3
    count of failed requests: 0
    statistics will be reset on: 1658996968724
    percentage of failed requests: 0.000000

#### Strategies details ####

* `ClosedCircuitBreakerStrategy` is implemented this way: it performs HTTP requests to the `product-service`,
  if the call failed - an empty list will be returned.

* `HalfOpenedCircuitBreakerStrategy` is implemented this way: it processes a special number of calls (in this implementation it is 5),
  if all those calls were failed, then it will be switched to the `OpenedCircuitBreakerStrategy`, otherwise, it will
  be switched to the `ClosedCircuitBreakerStrategy`.

* `OpenedCircuitBreakerStrategy` is implemented this way: it simply returns an empty result list without querying `product-service`.
  After each call, this strategy checks if it should be changed to the `HalfOpenedCircuitBreakerStrategy`.
  The `OpenedCircuitBreakerStrategy` will be changed to the `HalfOpenedCircuitBreakerStrategy` only
  after 15 seconds of usage of this strategy.

There is a special object that holds all the strategies: `CircuitBreakerStrategiesHolder`.
All the strategies use this object to return the following strategy.
For instance, if the percentage of failed calls >= 50%, then the `Closed`
the strategy should be replaced with the `Opened` strategy.
To perform this action `ClosedCircuitBreakerStrategy`
calls `CircuitBreakerStrategiesHolder.getOpenedCircuitBreakerStrategy()` method.
This getter is special only for the `Opened` strategy because it has to reset calls statistics for this strategy.
The reset method sets the time when the `Opened` strategy should be replaced with `Half-opened`.

To calculate percentage of failed requests, the logic has the variable: `INITIAL_CALLS_COUNT`.
The logic divides count of failed request by this variable. Such an approach was used,
to avoid immediate strategy changes. For instance if the logic made one request, and it failed,
if we divide count of failed requests by count of made calls - we would get 100% of failed calls,
which means that we have to move from `Closed` strategy to the `Opened`.

#### Product service ####

This service is intended to return products for order.
To test the circuit breaker special endpoint was added, which allows you to enable/disable this service.
By default, the product service is enabled.

    curl --location --request POST 'http://localhost:5055/product-service/configuration/enable'
    curl --location --request POST 'http://localhost:5055/product-service/configuration/disable'



