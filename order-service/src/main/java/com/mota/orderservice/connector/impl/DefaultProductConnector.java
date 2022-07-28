package com.mota.orderservice.connector.impl;

import com.mota.orderservice.annotation.Connector;
import com.mota.orderservice.connector.ProductConnector;
import com.mota.orderservice.dto.ProductDTO;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@Connector
@RequiredArgsConstructor
@Slf4j
public class DefaultProductConnector implements ProductConnector {

  private final ProductConnectorHelper productConnectorHelper;

  @Override
  public List<ProductDTO> getAllByProductIds(List<Integer> productIds) {
    Request request = productConnectorHelper.constructRequest(productIds);

    try (Response response = productConnectorHelper.executeRequest(request);
        ResponseBody responseBody = response.body()) {
      return productConnectorHelper.extractProductsFromResponse(responseBody);
    } catch (IOException e) {
      log.error(
          "Exception occurred during requesting products by ids. Empty product list will be returned."
              + " Exception: ", e);
    }
    return Collections.emptyList();
  }

  @Override
  public String getDetails() {
    return this.getClass().getSimpleName();
  }
}
