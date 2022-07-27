package com.mota.orderservice.dao;

import com.mota.orderservice.dao.entity.OrderEntity;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDAO extends CrudRepository<OrderEntity, String> {

  @Query("SELECT o FROM OrderEntity o")
  List<OrderEntity> getAll();

}
