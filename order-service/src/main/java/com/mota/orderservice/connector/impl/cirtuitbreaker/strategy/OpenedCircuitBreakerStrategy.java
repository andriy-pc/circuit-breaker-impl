package com.mota.orderservice.connector.impl.cirtuitbreaker.strategy;

import com.mota.orderservice.connector.impl.ProductConnectorHelper;
import com.mota.orderservice.dto.ProductDTO;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("openedCircuitBreakerStrategy")
public class OpenedCircuitBreakerStrategy extends
    AbstractCircuitBreakerStrategy {

  private static final Long STRATEGY_LIFE_MILLIS = Duration.ofSeconds(15).toMillis();

  private Long strategyStartEpochMillis = 0L;

  public OpenedCircuitBreakerStrategy(ProductConnectorHelper productConnectorHelper) {
    super(productConnectorHelper);
  }

  @Override
  public List<ProductDTO> applyStrategy(List<Integer> productIds) {
    return Collections.emptyList();
  }

  @Override
  public boolean shouldNewStrategyBeApplied() {
    return System.currentTimeMillis() > strategyStartEpochMillis + STRATEGY_LIFE_MILLIS;
  }

  @Override
  public CircuitBreakerStrategy getFollowingStrategy() {
    return circuitBreakerStrategiesHolder.getHalfOpenedCircuitBreakerStrategy();
  }

  @Override
  public void resetCallsStatistics() {
    super.resetCallsStatistics();
    strategyStartEpochMillis = System.currentTimeMillis();
  }
}
