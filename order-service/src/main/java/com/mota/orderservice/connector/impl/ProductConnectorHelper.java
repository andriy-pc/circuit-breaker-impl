package com.mota.orderservice.connector.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public final class ProductConnectorHelper {

  private final ObjectMapper objectMapper;
  private final OkHttpClient okHttpClient;

  private static final String PRODUCTS_URL_SEGMENT = "products";
  private static final String PRODUCT_IDS_QUERY_PARAM = "productIds";
  private static final String EMPTY_STRING = "";
  private static final String HTTP_PROTOCOL = "http";
  private static final String NON_DIGIT_REGEX = "\\D";

  @Value("${config.product-service.host}")
  private String productServiceHost;
  @Value("${config.product-service.port}")
  private String productServicePort;

  public Request constructRequest(List<Integer> productIds) {
    HttpUrl url = buildURL(productIds);

    Request request = new Request.Builder()
        .url(url)
        .get()
        .build();
    return request;
  }

  private HttpUrl buildURL(List<Integer> productIds) {
    Optional<String> queryParam = Optional.empty();
    try {
      queryParam = Optional.of(productIdsToQueryParam(productIds));
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

  private String productIdsToQueryParam(List<Integer> productIds) throws JsonProcessingException {
    return objectMapper.writeValueAsString(productIds)
        .replaceAll(NON_DIGIT_REGEX, EMPTY_STRING);
  }

  @NotNull
  public Response executeRequest(Request request) throws IOException {
    return okHttpClient.newCall(request).execute();
  }

  public List<ProductDTO> extractProductsFromResponse(ResponseBody responseBody)
      throws IOException {
    if (Objects.nonNull(responseBody)) {
      String responseBodyString = responseBody.string();
      return deserializeProducts(responseBodyString);
    } else {
      log.warn("Response body from product-service is NULL. Empty product list will be returned");
      return Collections.emptyList();
    }
  }

  public List<ProductDTO> deserializeProducts(String serializedProducts) {
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
