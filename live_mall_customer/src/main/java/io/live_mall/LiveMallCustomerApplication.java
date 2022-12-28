package io.live_mall;

import io.live_mall.wxpay.config.WxAppletsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableConfigurationProperties({WxAppletsProperties.class})
public class LiveMallCustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiveMallCustomerApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
