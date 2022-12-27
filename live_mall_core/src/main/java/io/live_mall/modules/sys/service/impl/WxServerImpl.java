package io.live_mall.modules.sys.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage.Data;
import io.live_mall.common.exception.RRException;
import io.live_mall.modules.server.dao.MemberTemplateMsgDao;
import io.live_mall.modules.server.entity.MemberTemplateMsgEntity;
import io.live_mall.modules.sys.entity.SysUserEntity;
import io.live_mall.modules.sys.service.WxService;
import lombok.extern.log4j.Log4j2;
import me.chanjar.weixin.common.error.WxErrorException;


@Log4j2
@Service
public class WxServerImpl implements  WxService {

 		
	@Autowired
    private WxMaService wxService;
	
	@Autowired
	MemberTemplateMsgDao memberTemplateMsgDao;
	
	
	@Override
	public String getPhone(JSONObject json,SysUserEntity user) {
		   WxMaJscode2SessionResult sessionInfo;
		try {
			sessionInfo = wxService.getUserService().getSessionInfo(json.getJSONObject("detial").getString("code"));
			user.setWxId(sessionInfo.getOpenid());
			WxMaPhoneNumberInfo phoneNoInfo = wxService.getUserService().getPhoneNoInfo(sessionInfo.getSessionKey(), json.getJSONObject("detial").getString("encryptedData"), json.getJSONObject("detial").getString("iv"));
			user.setMobile(phoneNoInfo.getPhoneNumber());
			return phoneNoInfo.getPhoneNumber();
		} catch (WxErrorException e) {
			// TODO Auto-generated catch block
			throw new RRException("asdadad");
		}
	}




	@Override
	public void wxMsgSend(MemberTemplateMsgEntity memberTemp) {
		// TODO Auto-generated method stub
		WxMaSubscribeMessage wxMaSubscribeMessage = new WxMaSubscribeMessage();
		String data = memberTemp.getData();
		List<Data> msgData = JSON.parseArray(data, Data.class);
		wxMaSubscribeMessage.setData(msgData);
		wxMaSubscribeMessage.setMiniprogramState(memberTemp.getWxVersion());
		wxMaSubscribeMessage.setToUser(memberTemp.getWxId());
		wxMaSubscribeMessage.setTemplateId(memberTemp.getTemplateId());
		if(StringUtils.isNotBlank(memberTemp.getPage())) {
			wxMaSubscribeMessage.setPage(memberTemp.getPage());
		}
		try {
			wxService.getMsgService().sendSubscribeMsg(wxMaSubscribeMessage);
			memberTemp.setCallbakMsg("发送成功");
			memberTemp.setStatus("1");
			if(memberTemp.getId() !=null) {
				memberTemplateMsgDao.updateById(memberTemp);
			}else {
				memberTemplateMsgDao.insert(memberTemp);
			}
		} catch (Exception e) {
			memberTemp.setCallbakMsg(e.getMessage());
			memberTemp.setStatus("2");
			if(memberTemp.getId() !=null) {
				memberTemplateMsgDao.updateById(memberTemp);
			}else {
				memberTemplateMsgDao.insert(memberTemp);
			}
		}
	
	}
	
}	
