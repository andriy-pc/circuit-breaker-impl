package com.mota.orderservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneralConfiguration {

  @Bean
  OkHttpClient okHttpClient() {
    return new OkHttpClient();
  }

  @Bean
  ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

}
