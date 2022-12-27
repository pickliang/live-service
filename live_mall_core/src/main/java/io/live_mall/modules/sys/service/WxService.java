package io.live_mall.modules.sys.service;


import com.alibaba.fastjson.JSONObject;

import io.live_mall.modules.server.entity.MemberTemplateMsgEntity;
import io.live_mall.modules.sys.entity.SysUserEntity;


public interface WxService {
	String getPhone(JSONObject json,SysUserEntity user);

	void wxMsgSend(MemberTemplateMsgEntity memberTemplateMsgEntity);
}
