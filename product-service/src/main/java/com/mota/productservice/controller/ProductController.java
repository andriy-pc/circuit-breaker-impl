package com.mota.productservice.controller;

import com.mota.productservice.dto.ProductDTO;
import com.mota.productservice.service.ProductService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @GetMapping
  public List<ProductDTO> getProductsByIds(@RequestParam(name = "productIds") List<String> productIdsString) {
    List<Integer> productIds = productIdsString.stream()
        .map(Integer::parseInt)
        .collect(Collectors.toList());
    return productService.getByProductIds(productIds);
  }

}
