package io.live_mall.modules.server.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage.Data;
import cn.hutool.core.date.DateUtil;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.MemberTemplateMsgDao;
import io.live_mall.modules.server.entity.MemberTemplateMsgEntity;
import io.live_mall.modules.server.entity.OrderEntity;
import io.live_mall.modules.server.service.MemberTemplateMsgService;
import io.live_mall.modules.server.utils.OrderEnum;
import io.live_mall.modules.sys.entity.SysUserEntity;
import io.live_mall.modules.sys.service.WxService;


@Service("memberTemplateMsgService")
public class MemberTemplateMsgServiceImpl extends ServiceImpl<MemberTemplateMsgDao, MemberTemplateMsgEntity> implements MemberTemplateMsgService {
	@Autowired
	MemberTemplateMsgDao memberTemplateMsgDao;
	
	@Autowired
	WxService wxService;
	

	@Value("${wechat.miniapp.version}")
	private String version;
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberTemplateMsgEntity> page = this.page(
                new Query<MemberTemplateMsgEntity>().getPage(params),
                new QueryWrapper<MemberTemplateMsgEntity>()
        );

        return new PageUtils(page);
    }

	@Override
	public void sendOrderIdentifyStartMsg(OrderEntity order) {
		// TODO Auto-generated method stub
		MemberTemplateMsgEntity memberTemplateMsgEntity = this.getOne(new QueryWrapper<MemberTemplateMsgEntity>().eq("bus_id", order.getId()).eq("status", 0).eq("user_id", order.getSaleId()).last("LIMIT 1"));
		if(memberTemplateMsgEntity !=null) {
			List<Data> templateMasg = crateTemplateMasg(memberTemplateMsgEntity.getTemplateId(), order);
			memberTemplateMsgEntity.setData(com.alibaba.fastjson.JSONArray.toJSONString(templateMasg));
			wxService.wxMsgSend(memberTemplateMsgEntity);
		}
		
		
	}
	
	
	public  List<Data> crateTemplateMasg(String id,OrderEntity order){
		 List<Data> dataList = new ArrayList<Data>();
		if("NQ-YeZdnOZfuwt7tQh-fV1j47tU8V1pZ1KwWhMDMqE8".equals(id)) {
			dataList.add(new Data("character_string1",order.getOrderNo()));
			dataList.add(new Data("time8",DateUtil.formatDateTime(new Date())));
			dataList.add(new Data("phrase3",OrderEnum.getV(order.getStatus())));
			dataList.add(new Data("thing6",order.getProduct().getProductName()));
			dataList.add(new Data("thing5","请查看订单详情"));
		}
		
		if("GMtUYCGV7sSl3L0UKgxSPxL5sJDBFnR9R6rRqPnp690".equals(id)) {
			dataList.add(new Data("character_string6",order.getOrderNo()));
			dataList.add(new Data("thing17",order.getProduct().getProductName()));	
			dataList.add(new Data("phrase2",OrderEnum.getV(order.getStatus())));
			dataList.add(new Data("date10",DateUtil.formatDateTime(new Date())));
			dataList.add(new Data("thing5","请查看订单详情"));
		}
		
		if("h8nJHWBDbob2z8umJj65CZUwxaNIOTHPyTK8d65-I3I".equals(id)) {
			dataList.add(new Data("character_string1",order.getOrderNo()));
			dataList.add(new Data("thing6",order.getProduct().getProductName()));	
			dataList.add(new Data("phrase2",OrderEnum.getV(order.getStatus())));
			dataList.add(new Data("date3",DateUtil.formatDateTime(new Date())));
			dataList.add(new Data("thing10","请查看订单详情"));
		}
		return dataList;
	}

	@Override
	public void saveTemplateMsg(SysUserEntity sysUserEntity, List<MemberTemplateMsgEntity> memberTemplateMsg) {
		// TODO Auto-generated method stub
		memberTemplateMsg.stream().forEach(e->{
			e.setUserId(sysUserEntity.getUserId());
			e.setMsgType("订单消息");
			e.setStatus("0");
			e.setWxId(sysUserEntity.getWxId());
			this.save(e);
		});
	}


}