package com.mota.orderservice.connector.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mota.orderservice.annotation.Connector;
import com.mota.orderservice.connector.ProductConnector;
import com.mota.orderservice.dto.ProductDTO;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Value;

@Connector
@RequiredArgsConstructor
@Slf4j
public class DefaultProductConnector implements ProductConnector {

  private static final String PRODUCTS_URL_SEGMENT = "products";
  private static final String PRODUCT_IDS_QUERY_PARAM = "productIds";
  private static final String EMPTY_STRING = "";
  private static final String HTTP_PROTOCOL = "http";
  private static final String NON_DIGIT_REGEX = "\\D";

  @Value("${config.product-service.host}")
  private String productServiceHost;
  @Value("${config.product-service.port}")
  private String productServicePort;

  private final OkHttpClient okHttpClient;
  private final ObjectMapper objectMapper;

  @Override
  public List<ProductDTO> getAllByProductIds(List<Integer> productIds) {
    HttpUrl url = buildURL(productIds);

    Request request = new Request.Builder()
        .url(url)
        .get()
        .build();

    try (Response response = okHttpClient.newCall(request).execute();
        ResponseBody responseBody = response.body()) {
      if (Objects.nonNull(responseBody)) {
        String responseBodyString = responseBody.string();
        return convertStringToObject(responseBodyString);
      } else {
        log.warn("Response body from product-service is NULL. Empty product list will be returned");
      }
    } catch (IOException e) {
      log.error("Exception occurred during requesting products by ids. Empty product list will be returned."
          + " Exception: ", e);
    }
    return Collections.emptyList();
  }

  private HttpUrl buildURL(List<Integer> productIds) {
    Optional<String> queryParam = Optional.empty();
    try {
      queryParam = Optional.of(ListToString(productIds));
    } catch (JsonProcessingException e) {
      log.error("Exception occurred during building request URL to get products by ids. "
          + "Empty request param will be returned. "
          + " Exception: ", e);
    }
    return new HttpUrl.Builder()
        .scheme(HTTP_PROTOCOL)
        .host(productServiceHost)
        .port(Integer.parseInt(productServicePort))
        .addPathSegment(PRODUCTS_URL_SEGMENT)
        .addQueryParameter(PRODUCT_IDS_QUERY_PARAM, queryParam.orElse(EMPTY_STRING))
        .build();
  }

  private String ListToString(List<Integer> productIds) throws JsonProcessingException {
    return objectMapper.writeValueAsString(productIds)
        .replaceAll(NON_DIGIT_REGEX, EMPTY_STRING);
  }

  private List<ProductDTO> convertStringToObject(String serializedProducts) {
    try {
      return objectMapper.readValue(serializedProducts, new TypeReference<List<ProductDTO>>() {
      });
    } catch (JsonProcessingException e) {
      log.error("Exception occurred during deserialization of products. "
          + "Empty list of products will be returned. Exception: ", e);
      return Collections.emptyList();
    }
  }
}
