package io.live_mall.modules.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.live_mall.common.utils.R;
import io.live_mall.common.utils.ShiroUtils;
import io.live_mall.modules.server.model.MemberModel;
import io.live_mall.modules.server.service.SmsService;


@RestController
@RequestMapping("/sms")
public class SmsController {
		
	@Autowired
	private SmsService smsService;
	
	/**
	 * 通过手机号发现短信验证码
	 * @param sweetsModel
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
	
}
