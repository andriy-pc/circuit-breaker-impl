package com.mota.orderservice.controller;

import com.mota.orderservice.dto.OrderDTO;
import com.mota.orderservice.service.OrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @GetMapping()
  public List<OrderDTO> getAllOrders() {
    return orderService.getAllOrders();
  }

}
