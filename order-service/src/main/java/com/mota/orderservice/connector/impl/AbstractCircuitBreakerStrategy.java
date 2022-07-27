package com.mota.orderservice.connector.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

@RequiredArgsConstructor
@Getter(AccessLevel.PROTECTED)
@Slf4j
public abstract class AbstractCircuitBreakerStrategy implements CircuitBreakerStrategy {

  private static final Long STATISTICS_LIVE_PERIOD_MILLIS = Duration.ofSeconds(5).toMillis();
  protected static final BigDecimal INITIAL_CALLS_COUNT = BigDecimal.valueOf(10);

  protected final DefaultProductConnector defaultProductConnector;
  protected CircuitBreakerStrategiesHolder circuitBreakerStrategiesHolder;
  private int countOfAllRequests = 0;
  private int failedRequestsCount = 0;
  private Long resetStatisticsEpochMillis = 0L;

  @Autowired
  @Lazy
  protected void setCircuitBreakerStrategiesHolder(
      CircuitBreakerStrategiesHolder circuitBreakerStrategiesHolder) {
    this.circuitBreakerStrategiesHolder = circuitBreakerStrategiesHolder;
  }

  protected void addCallResultToStatistics(Response response) {
    resetStatisticsIfNecessary();
    increaseCountOfMadeCalls();
    if (!response.isSuccessful()) {
      increaseCountOfFailedCalls();
    }
  }

  @Override
  public void resetCallsStatistics() {
    countOfAllRequests = 0;
    failedRequestsCount = 0;
    resetStatisticsEpochMillis = getResetStatisticsEpochMillis();
  }

  private long getResetStatisticsEpochMillis() {
    return System.currentTimeMillis() + STATISTICS_LIVE_PERIOD_MILLIS;
  }

  @Override
  public String getDetails() {
    return String.format("Strategy: %s\n"
            + "count of recorded requests: %d\n"
            + "count of failed request: %d\n"
            + "statistics will be reset on: %d\n"
            + "percentage of failed request: %f",
        this.getClass().getSimpleName(),
        countOfAllRequests,
        failedRequestsCount,
        resetStatisticsEpochMillis,
        getPercentageOfFailedRequests());
  }

  protected BigDecimal getPercentageOfFailedRequests() {
    return BigDecimal.valueOf(failedRequestsCount)
        .divide(INITIAL_CALLS_COUNT, 2, RoundingMode.HALF_UP);
  }

  protected synchronized void increaseCountOfMadeCalls() {
    countOfAllRequests += 1;
  }

  protected synchronized void increaseCountOfFailedCalls() {
    failedRequestsCount += 1;
  }

  protected synchronized void resetStatisticsIfNecessary() {
    if (System.currentTimeMillis() > resetStatisticsEpochMillis) {
      resetCallsStatistics();
    }
  }

}
