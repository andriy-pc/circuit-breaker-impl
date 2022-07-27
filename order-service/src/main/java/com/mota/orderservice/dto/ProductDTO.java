package com.mota.orderservice.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProductDTO {

  private Integer productId;
  private BigDecimal price;
  private Integer stockQuantity;

}
