package io.live_mall.modules.server.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.MemberTemplateMsgEntity;
import io.live_mall.modules.server.entity.OrderEntity;
import io.live_mall.modules.sys.entity.SysUserEntity;

/**
 * 会员消息订阅表
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-09-23 22:54:07
 */
public interface MemberTemplateMsgService extends IService<MemberTemplateMsgEntity> {
    PageUtils queryPage(Map<String, Object> params);

	/**
	 * 发送鉴定订单消息
	 */
	void sendOrderIdentifyStartMsg(OrderEntity order);

	void saveTemplateMsg(SysUserEntity sysUserEntity, List<MemberTemplateMsgEntity> memberTemplateMsg);
	
}

