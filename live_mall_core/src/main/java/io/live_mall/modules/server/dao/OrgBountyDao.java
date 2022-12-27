package io.live_mall.modules.server.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import io.live_mall.modules.server.entity.OrgBountyEntity;

/**
 * 
 * 
 * @author daitao
 * @email 867278141@qq.com
 * @date 2021-04-18 17:23:53
 */
@Mapper
public interface OrgBountyDao extends BaseMapper<OrgBountyEntity> {

	JSONObject getOne(@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("userId") Long userId);
	
}
