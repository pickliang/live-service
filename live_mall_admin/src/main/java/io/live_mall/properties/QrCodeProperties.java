package io.live_mall.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author yewl
 * @date 2023/1/4 15:40
 * @description
 */
@Getter
@Configuration
public class QrCodeProperties {
    @Value("${qrcode:url}")
    private String qrUrl;
}
