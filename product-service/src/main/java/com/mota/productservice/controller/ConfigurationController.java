package com.mota.productservice.controller;

import com.mota.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product-service/configuration")
@RequiredArgsConstructor
public class ConfigurationController {

  private final ProductService productService;

  @PostMapping("/disable")
  void disableService() {
    productService.disable();
  }

  @PostMapping("/enable")
  void enableService() {
    productService.enable();
  }

  @GetMapping("/status")
  String getStatus() {
    return productService.getStatus() ? "enabled" : "disabled";
  }


}
