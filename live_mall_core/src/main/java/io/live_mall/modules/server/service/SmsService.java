package io.live_mall.modules.server.service;


import com.alibaba.fastjson.JSONObject;
import io.live_mall.modules.server.entity.OrderEntity;
import io.live_mall.modules.server.model.MemberModel;

import java.util.Map;

public interface SmsService {
	/**
	 * 发送短信验证码
	 * @param phone
	 */
	int sendCode(String phone);

	void sendRegisterPhoneCode( MemberModel memberModel);

	void sendUptPhoneCode(MemberModel memberModel);
	
	int sendMsg(String phone, JSONObject contextJson, String template);

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
	 * @param phone 发送短信给用户
	 * @param order
	 */
	void sendMsgToCust(OrderEntity orderEntity);

	void mmsSend(Map<String, Object> params);
}
