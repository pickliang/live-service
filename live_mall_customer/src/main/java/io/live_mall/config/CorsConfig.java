package io.live_mall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

  @Bean
  public CorsFilter corsFilter() {
      CorsConfiguration config = new CorsConfiguration();
      // 该项设置为*表示允许所有地址来请求，将这个设置为*就简单的解决了跨域请求的问题了
      config.addAllowedOrigin("*");
      // 这三个属于允许请求中的其他一些信息
      config.addAllowedMethod("*");
      config.addAllowedHeader("*");
      config.addExposedHeader(HttpHeaders.ACCEPT);
      UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
      // 设置请求哪些资源会经过这个过滤器
      configSource.registerCorsConfiguration("/**",config);

      return new CorsFilter(configSource);
  }
}
