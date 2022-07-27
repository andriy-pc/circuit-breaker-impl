package com.mota.orderservice.connector.impl;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Component
@Scope(scopeName = "prototype")
public class ProductConnectorCallsStatistics {

  private int madeCallsCount = 0;
  private int successfulRequestsCount = 0;
  private int failedRequestsCount = 0;

  synchronized void increaseCountOfMadeCalls() {
    madeCallsCount += 1;
  }

  synchronized void increaseCountOfFailedCalls() {
    failedRequestsCount += 1;
  }

  synchronized void increaseCountOfSuccessfulCalls() {
    successfulRequestsCount += 1;
  }

}
