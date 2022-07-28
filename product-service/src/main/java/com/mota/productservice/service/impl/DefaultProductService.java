package com.mota.productservice.service.impl;

import com.mota.productservice.dao.ProductDAO;
import com.mota.productservice.dao.entity.ProductEntity;
import com.mota.productservice.dto.ProductDTO;
import com.mota.productservice.service.ProductService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultProductService implements ProductService {

  private final ProductDAO productDAO;

  private boolean enabled = true;

  @Override
  public void disable() {
    enabled = false;
  }

  @Override
  public void enable() {
    enabled = true;
  }

  @Override
  public boolean getStatus() {
    return enabled;
  }

  @Override
  public List<ProductDTO> getByProductIds(List<Integer> productIds) {
    if (!enabled) {
      throw new RuntimeException("Service is not responding!");
    }
    List<ProductEntity> productEntities = productDAO.getAllByProductIds(productIds);
    return productEntities.stream()
        .map(ProductDTO::new)
        .collect(Collectors.toList());
  }
}
