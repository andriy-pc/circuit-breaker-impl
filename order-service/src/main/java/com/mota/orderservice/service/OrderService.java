package com.mota.orderservice.service;

import com.mota.orderservice.dto.OrderDTO;
import java.util.List;

public interface OrderService {

  List<OrderDTO> getAllOrders();

}
