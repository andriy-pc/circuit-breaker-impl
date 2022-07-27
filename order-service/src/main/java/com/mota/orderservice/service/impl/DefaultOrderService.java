package com.mota.orderservice.service.impl;

import com.mota.orderservice.dao.OrderDAO;
import com.mota.orderservice.dao.entity.OrderEntity;
import com.mota.orderservice.dto.OrderDTO;
import com.mota.orderservice.mapper.OrderMapper;
import com.mota.orderservice.service.OrderService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultOrderService implements OrderService {

  private final OrderDAO orderDao;

  private final OrderMapper orderMapper;

  @Override
  public List<OrderDTO> getAllOrders() {
    List<OrderEntity> orderEntities = orderDao.getAll();
    return orderEntities.stream()
        .map(orderMapper::toDTO)
        .collect(Collectors.toList());
  }
}
