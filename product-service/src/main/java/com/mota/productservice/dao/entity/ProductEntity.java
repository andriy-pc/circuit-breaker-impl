package com.mota.productservice.dao.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class ProductEntity {

  @Id
  @Column(name = "product_id")
  private Integer productId;

  @Column(name = "stock_quantity")
  private Integer stockQuantity;

  @Column(name = "price")
  private BigDecimal price;

}
