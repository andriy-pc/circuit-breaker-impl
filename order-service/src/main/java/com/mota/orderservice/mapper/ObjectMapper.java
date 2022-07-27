package com.mota.orderservice.mapper;

public interface ObjectMapper<E, D> {

  D toDTO(E entity);

}
