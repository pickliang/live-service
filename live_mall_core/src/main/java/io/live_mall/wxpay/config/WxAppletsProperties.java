package io.live_mall.wxpay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author yewl
 * @date 2022/12/28 12:51
 * @description
 */
@Data
@ConfigurationProperties(prefix = "wx.applets")
public class WxAppletsProperties {

    private String appid;

    private String secret;
}
