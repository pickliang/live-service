package io.live_mall.modules.server.dao;

import io.live_mall.modules.server.entity.SysOrgUserEntity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-27 12:21:45
 */
@Mapper
public interface SysOrgUserDao extends BaseMapper<SysOrgUserEntity> {

	JSONObject getType(Long userId);
	
}
