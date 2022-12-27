package io.live_mall.modules.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import io.live_mall.common.utils.R;
import io.live_mall.common.utils.ShiroUtils;
import io.live_mall.modules.sys.service.SysUserService;



@RestController
@RequestMapping("/server/wx")
public class WxController {
	
	Logger logger=LoggerFactory.getLogger(WxController.class);
	
	@Autowired
	SysUserService userService;
	
	@RequestMapping(value = "wxMemberPhone", method = RequestMethod.POST)
	public R wxMemberPhone( @RequestBody JSONObject userJson) {
		Long userId = ShiroUtils.getUserId();
		return R.ok().put("data", userService.wxPhone(userJson,userId));
	}
}
