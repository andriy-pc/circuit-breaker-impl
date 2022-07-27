package com.mota.orderservice.dao.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;
import lombok.Data;

@Data
@Embeddable
@Table(name = "order_product")
public class OrderProductEntity {

  @Column(name = "product_id")
  private Integer productId;

  @Column(name = "ordered_qty")
  private Integer orderedQty;

}
