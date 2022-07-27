package com.mota.orderservice.connector.impl;

import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Data
public class CircuitBreakerStrategiesHolder {

  public CircuitBreakerStrategiesHolder(
      @Qualifier("closedCircuitBreakerStrategy") CircuitBreakerStrategy closedCircuitBreakerStrategy,
      @Qualifier("openedCircuitBreakerStrategy") CircuitBreakerStrategy openedCircuitBreakerStrategy,
      @Qualifier("halfOpenedCircuitBreakerStrategy") CircuitBreakerStrategy halfOpenedCircuitBreakerStrategy) {
    this.closedCircuitBreakerStrategy = closedCircuitBreakerStrategy;
    this.openedCircuitBreakerStrategy = openedCircuitBreakerStrategy;
    this.halfOpenedCircuitBreakerStrategy = halfOpenedCircuitBreakerStrategy;
  }

  private final CircuitBreakerStrategy closedCircuitBreakerStrategy;
  private final CircuitBreakerStrategy openedCircuitBreakerStrategy;
  private final CircuitBreakerStrategy halfOpenedCircuitBreakerStrategy;

  public CircuitBreakerStrategy getOpenedCircuitBreakerStrategy() {
    openedCircuitBreakerStrategy.resetCallsStatistics();
    return openedCircuitBreakerStrategy;
  }


}
