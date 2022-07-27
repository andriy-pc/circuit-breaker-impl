package com.mota.productservice.dao;

import com.mota.productservice.dao.entity.ProductEntity;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ProductDAO extends CrudRepository<ProductEntity, Integer> {

  @Query("SELECT p FROM ProductEntity p WHERE p.productId IN :productIds")
  List<ProductEntity> getAllByProductIds(List<Integer> productIds);

}
