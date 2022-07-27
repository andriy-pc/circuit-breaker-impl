package com.mota.orderservice.dto;

import com.mota.orderservice.dao.entity.OrderEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDTO {

  public OrderDTO(OrderEntity entity) {
    this.originalOrderId = entity.getOriginalOrderId();
    this.price = entity.getOrderPrice();
  }

  private String originalOrderId;

  private List<ProductDTO> products = new ArrayList<>();

  private BigDecimal price;

}
