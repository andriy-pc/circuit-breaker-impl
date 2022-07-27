package com.mota.productservice.service;

import com.mota.productservice.dto.ProductDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductService {

  List<ProductDTO> getByProductIds(List<Integer> productIds);

}
