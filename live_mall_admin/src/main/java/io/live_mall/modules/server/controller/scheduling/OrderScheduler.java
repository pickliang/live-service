package io.live_mall.modules.server.controller.scheduling;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import io.live_mall.modules.server.entity.OrderEntity;
import io.live_mall.modules.server.entity.RaiseEntity;
import io.live_mall.modules.server.model.OrderUtils;
import io.live_mall.modules.server.service.OrderService;
import io.live_mall.modules.server.service.RaiseService;
import io.live_mall.modules.server.service.SmsService;
import lombok.extern.slf4j.Slf4j;

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
	
}
