package io.live_mall.modules.server.controller;

import io.live_mall.common.utils.R;
import io.live_mall.common.utils.RedisUtils;
import io.live_mall.common.utils.ShiroUtils;
import io.live_mall.constants.RedisKeyConstants;
import io.live_mall.modules.applets.AppletsService;
import io.live_mall.modules.server.model.MemberModel;
import io.live_mall.modules.server.service.OrderService;
import io.live_mall.modules.server.service.SmsService;
import io.live_mall.sms.mms.MmsClient;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/sms")
public class SmsController {
		
	@Autowired
	private SmsService smsService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private RedisUtils redisUtils;
	@Autowired
	private AppletsService appletsService;

	/**
	 * 通过手机号发现短信验证码
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value="sendCode/{mobile}",method=RequestMethod.GET)
	public R sendCode(@PathVariable("mobile") String mobile){
		smsService.sendCode(mobile);
		return R.ok();
	}
	
	@RequestMapping(value="sendUptPhoneCode/{mobile}",method=RequestMethod.POST)
	public R sendUptPhoneCode(@PathVariable("mobile") String mobile){
		MemberModel memberModel = new MemberModel();
		memberModel.setUserId(ShiroUtils.getUserId());
		memberModel.setMobile(mobile);
		smsService.sendUptPhoneCode(memberModel);
		return R.ok();
	}

	/**
	 * 发送对付通知短信
	 * @param params
	 * @return
	 */
	@PostMapping(value = "/mms-send")
	@SneakyThrows
	public R mmsSend(@RequestBody Map<String, Object> params) {
		String token = redisUtils.get(RedisKeyConstants.SMS_TOKEN);
		if (StringUtils.isBlank(token)) {
			token = MmsClient.getToken();
			redisUtils.set(RedisKeyConstants.SMS_TOKEN, token, RedisUtils.DEFAULT_EXPIRE);
		}
		String envVersion = String.valueOf(params.get("envVersion"));
		String urlLink = appletsService.getUrlLink(envVersion);
		String startDate = String.valueOf(params.get("startDate"));
		String endDate = String.valueOf(params.get("endDate"));
		String ids = String.valueOf(params.get("ids"));

		orderService.selectDuifuNoticeData(startDate, endDate, ids, urlLink, token, ShiroUtils.getUserId());
		return R.ok();
	}
}
