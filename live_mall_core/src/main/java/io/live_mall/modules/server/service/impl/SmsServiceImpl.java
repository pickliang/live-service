package io.live_mall.modules.server.service.impl;

import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.live_mall.common.exception.RRException;
import io.live_mall.common.template.CommonConstant;
import io.live_mall.common.template.MobileMsgTemplate;
import io.live_mall.common.utils.RedisUtils;
import io.live_mall.modules.server.entity.OrderEntity;
import io.live_mall.modules.server.model.MemberModel;
import io.live_mall.modules.server.service.MemberTemplateMsgService;
import io.live_mall.modules.server.service.SmsService;
import io.live_mall.modules.sys.dao.SysUserDao;
import io.live_mall.modules.sys.entity.SysUserEntity;
import io.live_mall.sms.handle.SmsAliyunMessageHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

	@Autowired
	RedisUtils redisUtils;

	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@Autowired
	SmsAliyunMessageHandler smsAliyunMessageHandler;

	@Autowired
	SysUserDao sysUserDao;
	
	
	@Autowired
	MemberTemplateMsgService memberTemplateMsgService;
	

	@Override
	public int sendCode(String phone) {
		if (StringUtils.isBlank(phone)) {
			throw new RRException("手机号必填");
		}
		if (!phone.matches("\\d{11}")) {
			throw new RRException("手机号必须为11位的号码");
		}
		String code = redisUtils.get("sms:" + phone);

		if (StringUtils.isBlank(code)) {
			//验证手机号
			String randCode = getRandCode(4);
			log.info("发送验证码为" + randCode);
			JSONObject contextJson = new JSONObject();
			contextJson.put("code", randCode);
			MobileMsgTemplate mobileMsgTemplate = new MobileMsgTemplate(phone, contextJson.toJSONString(),
					CommonConstant.ALIYUN_SMS, "分销峰", "loginCodeChannel");
			/*stringRedisTemplate.convertAndSend("sendMsg", JSONObject.toJSONString(mobileMsgTemplate));*/
			smsAliyunMessageHandler.process(mobileMsgTemplate);
			redisUtils.set("sms:" + phone, randCode, 300);
			return 1;
		} else {
			log.info("发送验证码为" + code);
			throw new RRException("验证码已发送");
		}
	}

	@Override
	public int sendMsg(String phone, JSONObject contextJson, String template) {
		if (StringUtils.isBlank(phone)) {
			throw new RRException("手机号必填");
		}
		if (!phone.matches("\\d{11}")) {
			throw new RRException("手机号必须为11位的号码");
		}
		MobileMsgTemplate mobileMsgTemplate = new MobileMsgTemplate(phone, contextJson.toJSONString(),
				CommonConstant.ALIYUN_SMS, "私募项目", template);
		smsAliyunMessageHandler.process(mobileMsgTemplate);
		return 1;

	}

	private static String[] rands = { "1", "2", "3", "4", "5", "6", "7", "8", "9" };

	private static String getRandCode(int len) {
		StringBuilder sb = new StringBuilder();
		Random r = new Random();
		for (int i = 0; i < len; i++) {
			sb.append(rands[r.nextInt(rands.length)]);
		}
		return sb.toString();
	}

	@Override
	public void sendRegisterPhoneCode(MemberModel memberModel) {
		SysUserEntity selectOne = sysUserDao
				.selectOne(new QueryWrapper<SysUserEntity>().eq("mobile", memberModel.getMobile()));
		if (selectOne != null) {
			throw new RRException("手机号以被使用");
		}
		sendCode(memberModel.getCode());
	}

	@Override
	public void sendUptPhoneCode(MemberModel memberModel) {
		SysUserEntity selectOne = sysUserDao
				.selectOne(new QueryWrapper<SysUserEntity>().eq("mobile", memberModel.getMobile()));
		if (selectOne != null && memberModel.getUserId().equals(selectOne.getUserId())) {
			throw new RRException("手机号以被使用");
		}
		sendCode(memberModel.getCode());
	}
	
	
	/**
	 * 	/**
	 *  -1待报备
	 *  0:待审核  
	 *   1:已驳回
	 *    2：已过审
	 *    3 ：已退款
	 *     4：已成立
	 *      5：已结束
	 *       6：已退回 
	 * @param phone
	 * @param order
	 */
	@Override
	public void sendMsgToCust(OrderEntity order) {
		// TODO Auto-generated method stub
		SysUserEntity sysUserEntity = sysUserDao.selectById(order.getSaleId());
		String phone = sysUserEntity.getMobile();
		if(StringUtils.isBlank(phone)) {
			return;
		}
	    if(order.getStatus()== 2){
		   JSONObject contextJson = new JSONObject();
		   contextJson.put("custName", order.getCustomerName());
		   contextJson.put("productName", order.getProduct().getProductName());
		   //sendMsg(phone, contextJson, "productAplly");
		   memberTemplateMsgService.sendOrderIdentifyStartMsg(order);
	    }
	   //预约已失效
	   if(order.getStatus() == 5){
		   JSONObject contextJson = new JSONObject();
		   contextJson.put("custName", order.getCustomerName());
		   contextJson.put("productName", order.getProduct().getProductName());
		   //sendMsg(phone, contextJson, "productCancel");
		   memberTemplateMsgService.sendOrderIdentifyStartMsg(order);
	   }
	   
	   //订单退回
	   if(order.getStatus()== 6){
		   JSONObject contextJson = new JSONObject();
		   contextJson.put("custName", order.getCustomerName());
		   contextJson.put("productName", order.getProduct().getProductName());
		   contextJson.put("gdesc", order.getAduitResult());
		   //sendMsg(phone, contextJson, "productBack");
		   memberTemplateMsgService.sendOrderIdentifyStartMsg(order);
	   }
	   //订单驳回
	   if(order.getStatus() == 1){
		   JSONObject contextJson = new JSONObject();
		   contextJson.put("custName", order.getCustomerName());
		   contextJson.put("productName", order.getProduct().getProductName());
		   contextJson.put("gdesc", order.getAduitResult());
		   //sendMsg(phone, contextJson, "productRb");
		   memberTemplateMsgService.sendOrderIdentifyStartMsg(order);
	   }
	   //产品已成立
	   if(order.getStatus() == 4){
		   JSONObject contextJson = new JSONObject();
		   contextJson.put("custName", order.getCustomerName());
		   contextJson.put("productName", order.getProduct().getProductName());
		   //sendMsg(phone, contextJson, "productSuc");
		   memberTemplateMsgService.sendOrderIdentifyStartMsg(order);
	   }
	   
	}

}
