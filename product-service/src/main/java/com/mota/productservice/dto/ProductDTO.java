package com.mota.productservice.dto;

import com.mota.productservice.dao.entity.ProductEntity;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDTO {

  private Integer productId;
  private Integer stockQuantity;
  private BigDecimal price;

  public ProductDTO(ProductEntity entity) {
    this.productId = entity.getProductId();
    this.stockQuantity = entity.getStockQuantity();
    this.price = entity.getPrice();
  }
}
