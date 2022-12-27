package io.live_mall.modules.server.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.live_mall.modules.server.entity.SysOrgGroupEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 机构组织管理
 * 
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-27 12:21:45
 */
@Mapper
public interface SysOrgGroupDao extends BaseMapper<SysOrgGroupEntity> {

	List<JSONObject> getOrgView();

	JSONObject getOrgOneView(String orgId);

	List<String> getOrgIds(String id);
	
	List<SysOrgGroupEntity> getListByParentId(String  orgId);
	
	List<SysOrgGroupEntity> getListByName(String  organizationName);
	
	List<SysOrgGroupEntity> getListByParentIdIsNull();

	void removeChild(String ids);

	List<String> orgGroupIds(String name);
	
}
