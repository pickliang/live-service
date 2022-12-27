package io.live_mall.modules.server.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.MemberEntity;
import io.live_mall.modules.server.entity.OrderEntity;

import java.util.Map;

/**
 * 
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-12-09 22:21:50
 */
public interface MemberService extends IService<MemberEntity> {


	MemberEntity  saveOrUpdate(OrderEntity order);

	PageUtils queryPage(JSONObject params);
	
	void getMemberType(MemberEntity member);

	JSONObject getTj(String custId, String salesId);
}

