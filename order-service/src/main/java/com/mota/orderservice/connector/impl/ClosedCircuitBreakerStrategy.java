package com.mota.orderservice.connector.impl;

import com.mota.orderservice.dto.ProductDTO;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Qualifier("closedCircuitBreakerStrategy")
public class ClosedCircuitBreakerStrategy extends AbstractCircuitBreakerStrategy {

  public ClosedCircuitBreakerStrategy(DefaultProductConnector defaultProductConnector) {
    super(defaultProductConnector);
  }

  @Override
  public List<ProductDTO> applyStrategy(List<Integer> productIds) {
    Request request = defaultProductConnector.constructRequest(productIds);
    try (Response response = defaultProductConnector.executeRequest(request);
        ResponseBody responseBody = response.body()) {
      addCallResultToStatistics(response);
      if (!response.isSuccessful()) {
        return Collections.emptyList();
      }
      return defaultProductConnector.extractProductsFromResponse(responseBody);
    } catch (IOException e) {
      increaseCountOfMadeCalls();
      increaseCountOfFailedCalls();
      log.error(
          "Exception occurred during requesting products by ids. Empty product list will be returned."
              + " Exception: ", e);
    }
    return Collections.emptyList();
  }


  @Override
  public boolean shouldNewStrategyBeApplied() {
    return getPercentageOfFailedRequests().compareTo(BigDecimal.valueOf(0.5)) > 0;
  }

  @Override
  public CircuitBreakerStrategy getFollowingStrategy() {
    return circuitBreakerStrategiesHolder.getOpenedCircuitBreakerStrategy();
  }

}
