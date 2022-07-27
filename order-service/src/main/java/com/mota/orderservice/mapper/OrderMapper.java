package com.mota.orderservice.mapper;

import com.mota.orderservice.annotation.Mapper;
import com.mota.orderservice.connector.ProductConnector;
import com.mota.orderservice.dao.entity.OrderEntity;
import com.mota.orderservice.dao.entity.OrderProductEntity;
import com.mota.orderservice.dto.OrderDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Mapper
@RequiredArgsConstructor
public class OrderMapper implements ObjectMapper<OrderEntity, OrderDTO> {

  private final ProductConnector productConnector;

  @Override
  public OrderDTO toDTO(OrderEntity entity) {
    OrderDTO orderDTO = new OrderDTO(entity);
    List<Integer> orderProductIds = entity
        .getProducts().stream()
        .map(OrderProductEntity::getProductId).toList();
    orderDTO.setProducts(productConnector.getAllByProductIds(orderProductIds));
    return orderDTO;
  }

}
