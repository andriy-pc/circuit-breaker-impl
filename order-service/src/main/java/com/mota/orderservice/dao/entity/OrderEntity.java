package com.mota.orderservice.dao.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "orders")
@Data
public class OrderEntity {

  @Id
  @Column(name = "original_order_id")
  private String originalOrderId;

  @ElementCollection
  @CollectionTable(name = "order_product",
      joinColumns = {@JoinColumn(name = "original_order_id")})
  @Column(name = "product_id")
  private List<OrderProductEntity> products = new ArrayList<>();

  @Column(name = "order_price")
  private BigDecimal orderPrice;


}
