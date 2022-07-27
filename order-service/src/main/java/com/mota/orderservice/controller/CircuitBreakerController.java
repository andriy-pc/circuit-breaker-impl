package com.mota.orderservice.controller;

import com.mota.orderservice.connector.ProductConnector;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/circuit-breaker")
@RequiredArgsConstructor
public class CircuitBreakerController {

  private final ProductConnector productConnectorProxy;

  @GetMapping("/details")
  public String getDetailsAboutCircuitBreaker() {
    return productConnectorProxy.getDetails();
  }

}
