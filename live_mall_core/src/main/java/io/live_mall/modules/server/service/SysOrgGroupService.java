package io.live_mall.modules.server.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.modules.server.entity.SysOrgGroupEntity;

import java.util.List;
import java.util.Map;

/**
 * 机构组织管理
 *
 * @author daitao
 * @email 867278141@qq.com
 * @date 2020-11-27 12:21:45
 */
public interface SysOrgGroupService extends IService<SysOrgGroupEntity> {

	public PageUtils queryPage(Map<String, Object> params);

	List<SysOrgGroupEntity> listTree(String name);


	String getChildrenOrgIdAndCurrentId(String parentId);

	public List<JSONObject> getOrgView();
	
	JSONObject getOrgOneView(String orgId);

	public void removeByIds(String ids);

	String orgGroupIds(String name);
	
}

