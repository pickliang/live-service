/**
 * Copyright (c) 2016-2019 分销峰开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.live_mall;

import io.live_mall.wxpay.config.WxAppletsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableWebSocket
@SpringBootApplication
@EnableConfigurationProperties(WxAppletsProperties.class)
public class liveMallAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(liveMallAdminApplication.class, args);
	}

}