package com.mota.orderservice.connector.impl.cirtuitbreaker.strategy;

import com.mota.orderservice.connector.impl.ProductConnectorHelper;
import com.mota.orderservice.dto.ProductDTO;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("halfOpenedCircuitBreakerStrategy")
public class HalfOpenedCircuitBreakerStrategy extends ClosedCircuitBreakerStrategy {

  private static final Integer COUNT_OF_ALLOWED_CALLS = 5;

  public HalfOpenedCircuitBreakerStrategy(ProductConnectorHelper productConnectorHelper) {
    super(productConnectorHelper);
  }

  @Override
  public List<ProductDTO> applyStrategy(List<Integer> productIds) {
    if (getCountOfAllRequests() < COUNT_OF_ALLOWED_CALLS) {
      return super.applyStrategy(productIds);
    } else {
      return Collections.emptyList();
    }
  }

  @Override
  public boolean shouldNewStrategyBeApplied() {
    return getCountOfAllRequests() == COUNT_OF_ALLOWED_CALLS;
  }

  @Override
  public CircuitBreakerStrategy getFollowingStrategy() {
    if (openCircuit()) {
      return circuitBreakerStrategiesHolder.getOpenedCircuitBreakerStrategy();
    } else {
      return circuitBreakerStrategiesHolder.getClosedCircuitBreakerStrategy();
    }
  }

  @Override
  protected void addCallResultToStatistics(Response response) {
    increaseCountOfMadeCalls();
    if (!response.isSuccessful()) {
      increaseCountOfFailedCalls();
    }
  }

  private boolean openCircuit() {
    return getPercentageOfFailedRequests().compareTo(BigDecimal.ONE) >= 0;
  }
}
