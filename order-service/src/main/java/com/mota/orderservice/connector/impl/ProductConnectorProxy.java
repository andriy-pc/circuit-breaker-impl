package com.mota.orderservice.connector.impl;

import com.mota.orderservice.connector.ProductConnector;
import com.mota.orderservice.connector.impl.cirtuitbreaker.strategy.CircuitBreakerStrategy;
import com.mota.orderservice.dto.ProductDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductConnectorProxy implements ProductConnector {

  public ProductConnectorProxy(
      @Qualifier("closedCircuitBreakerStrategy") CircuitBreakerStrategy circuitBreakerStrategy) {
    this.circuitBreakerStrategy = circuitBreakerStrategy;
  }

  private CircuitBreakerStrategy circuitBreakerStrategy;

  @Override
  public List<ProductDTO> getAllByProductIds(List<Integer> productIds) {
    List<ProductDTO> productDTOS = circuitBreakerStrategy.applyStrategy(productIds);
    processConnectorCallsStatistics();
    return productDTOS;

  }

  private void processConnectorCallsStatistics() {
    if (circuitBreakerStrategy.shouldNewStrategyBeApplied()) {
      changeCircuitBreakerStrategy();
    }
  }

  private void changeCircuitBreakerStrategy() {
    circuitBreakerStrategy = circuitBreakerStrategy.getFollowingStrategy();
    circuitBreakerStrategy.resetCallsStatistics();
  }

  public String getDetails() {
    return circuitBreakerStrategy.getDetails();
  }
}
