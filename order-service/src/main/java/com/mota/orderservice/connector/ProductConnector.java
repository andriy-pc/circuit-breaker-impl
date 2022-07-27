package com.mota.orderservice.connector;

import com.mota.orderservice.dto.ProductDTO;
import java.util.List;

public interface ProductConnector {

  List<ProductDTO> getAllByProductIds(List<Integer> productIds);

}
