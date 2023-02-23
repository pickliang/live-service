package io.live_mall.modules.server.controller.scheduling;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.youzan.cloud.open.sdk.common.exception.SDKException;
import com.youzan.cloud.open.sdk.core.oauth.model.OAuthToken;
import com.youzan.cloud.open.sdk.gen.v4_0_2.model.YouzanTradesSoldGetResult;
import io.live_mall.common.utils.RedisUtils;
import io.live_mall.constants.RedisKeyConstants;
import io.live_mall.modules.server.entity.OrderEntity;
import io.live_mall.modules.server.entity.RaiseEntity;
import io.live_mall.modules.server.entity.YouZanUserEntity;
import io.live_mall.modules.server.model.OrderUtils;
import io.live_mall.modules.server.service.*;
import io.live_mall.tripartite.YouZanClients;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@EnableScheduling
public class OrderScheduler {
	
	@Autowired
	OrderService orderSeverice;
	
	@Autowired
	RaiseService raiseService;
	
	@Autowired
	SmsService smsService;
	@Autowired
	private YouZanUserService youZanUserService;
	@Autowired
	private RedisUtils redisUtils;
	@Autowired
	private YouZanOrderService youZanOrderService;
	
	@Scheduled(cron = "0 5/5 * * * ?")
	public void orderEnd(){
		//获取待报备的数据
		List<OrderEntity> list = orderSeverice.list(new QueryWrapper<OrderEntity>().in("status", -1,1));
		for (OrderEntity orderEntity : list) {
			RaiseEntity raiseEntity = raiseService.getById(orderEntity.getRaiseId());
			if(raiseEntity ==null ) {
				continue;
			}
			try {
				if("0".equals(raiseEntity.getDeadlineTimeType())) {
					Date endDate = orderEntity.getAppointTime();
					String[] split = raiseEntity.getDeadlineTime().split(",");
					if(split.length > 3){
						endDate = DateUtil.offsetDay(endDate,Integer.valueOf(split[0]));
						endDate =DateUtil.offsetHour(endDate, Integer.valueOf(split[1]));
						endDate =DateUtil.offsetMinute(endDate, Integer.valueOf(split[2]));
						if(DateUtil.compare(new Date(), endDate) > 0) {
							orderEntity.setStatus(5);
							orderSeverice.updateById(orderEntity);
							OrderUtils.orderStup(raiseEntity, orderEntity,5, "");
							smsService.sendMsgToCust(orderEntity);
						}
					}
				}else {
					DateTime endDate = DateUtil.offsetHour(orderEntity.getAppointTime(), Integer.valueOf(raiseEntity.getDeadlineTime()));
					if(DateUtil.compare(new Date(), endDate) > 0) {
						orderEntity.setStatus(5);
						OrderUtils.orderStup(raiseEntity, orderEntity,5, "");
						orderSeverice.updateById(orderEntity);
						smsService.sendMsgToCust(orderEntity);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				log.error("OrderScheduler 预约结束",e);
			}
		}
	}

	/**
	 * 每周六的5点执行
	 */
	@Scheduled(cron = "0 0 5 ? * SAT")
	public void yzOrder() {
		CompletableFuture.supplyAsync(() -> syncYzOrder());
	}

	private String syncYzOrder() {
		List<YouZanUserEntity> users = youZanUserService.list(Wrappers.lambdaQuery(YouZanUserEntity.class));
		users.forEach(user -> {
			try {
				String token = redisUtils.get(RedisKeyConstants.YZ_TOKEN);
				if (StringUtils.isBlank(token)) {
					OAuthToken authToken = YouZanClients.token();
					redisUtils.set(RedisKeyConstants.YZ_TOKEN, authToken.getAccessToken(), authToken.getExpires());
					token = authToken.getAccessToken();
				}
				YouzanTradesSoldGetResult.YouzanTradesSoldGetResultData data = null;
				data = YouZanClients.orderList(token, user.getYzOpenId());
				youZanOrderService.save(user.getYzOpenId(), data);
				Long totalResults = data.getTotalResults();
				while (100 == totalResults) {
					data = YouZanClients.orderList(token, user.getYzOpenId());
					youZanOrderService.save(user.getYzOpenId(), data);
				}
			} catch (SDKException e) {
				log.error("有赞订单异常--->{}", e);
			}
		});
		return null;

	}
}
