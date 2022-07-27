package com.mota.orderservice.connector.impl;

import com.mota.orderservice.dto.ProductDTO;
import java.util.List;

public interface CircuitBreakerStrategy {

  List<ProductDTO> applyStrategy(List<Integer> productIds);

  void resetCallsStatistics();

  boolean shouldNewStrategyBeApplied();

  CircuitBreakerStrategy getFollowingStrategy();

  String getDetails();


}
